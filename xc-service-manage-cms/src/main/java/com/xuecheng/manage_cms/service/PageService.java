package com.xuecheng.manage_cms.service;

import com.alibaba.fastjson.JSON;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.CustomExeception;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.config.RabbitmqConfig;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Service
public class PageService {
    @Autowired
    CmsPageRepository cmsPageRepository;

    /**
     * 复杂条件查询
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        //防止后面赋值判断空指针
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }

        if (page <= 0) {
            page = 1;
        }
        page = page - 1;//为了适应用户习惯
        if (size <= 0) {
            size = 20;
        }
        Pageable pageable = new PageRequest(page, size);

        CmsPage cmsPage = new CmsPage();

        //设置条件值（站点id）
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置模板id作为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //设置页面别名作为查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage, matcher);
        Page<CmsPage> cmsPages = cmsPageRepository.findAll(example, pageable);
        //封装结果
        QueryResponseResult<CmsPage> queryResponseResult = new QueryResponseResult<>(CommonCode.SUCCESS, cmsPages.getContent(), cmsPages.getTotalElements());
        return queryResponseResult;
    }

    /**
     * @description: 新增页面
     * @params: [cmsPage]
     * @date: Create in 17:30 2018/12/27
     */
    public CmsPageResult add(CmsPage cmsPage) {
        if (cmsPage == null) {
            //非法参数
            throw new CustomExeception(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        //查询
        CmsPage cmsPage1 = cmsPageRepository.findBySiteIdAndPageNameAndAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        //判断是否有索引重复，重复不添加
        if (cmsPage1 != null) {
            //页面已存在
            throw new CustomExeception(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        cmsPageRepository.save(cmsPage);
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
    }

    /**
     * @description: 根据id查找
     * @params: [pageId]
     * @date: Create in 19:37 2018/12/27
     */
    public CmsPage findById(String pageId) {
        CmsPage cmsPage = cmsPageRepository.findByPageId(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        return cmsPage;
    }

    /**
     * @description: 更新
     * @params: [cmsPage]
     * @date: Create in 22:48 2018/12/27
     */
    public CmsPageResult update(CmsPage cmsPage) {
        CmsPage one = findById(cmsPage.getPageId());
        if (one == null) {
            throw new CustomExeception(CmsCode.CMS_COURSE_PERVIEWISNULL);
        }
        //更新模板id
        one.setTemplateId(cmsPage.getTemplateId());
        //更新所属站点
        one.setSiteId(cmsPage.getSiteId());
        //更新页面别名
        one.setPageAliase(cmsPage.getPageAliase());
        //更新页面名称
        one.setPageName(cmsPage.getPageName());
        //更新访问路径
        one.setPageWebPath(cmsPage.getPageWebPath());
        //更新物理路径
        one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
        //更新数据地址
        one.setDataUrl(cmsPage.getDataUrl());
        //提交修改
        cmsPageRepository.save(one);
        return new CmsPageResult(CommonCode.SUCCESS, one);
    }

    /**
     * @description: 删除
     * @params: [pageId]
     * @date: Create in 22:47 2018/12/27
     */
    public ResponseResult del(String pageId) {
        try {
            cmsPageRepository.deleteById(pageId);
            return new ResponseResult(CommonCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(CommonCode.FAIL);
        }
    }

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    /**
     * 页面静态化
     * @param id
     * @return
     */
    public String getPageHtml(String id) {
        //获取模板数据
        Map model = getModelByPageId(id);
        //获取模板
        String template = getTemplate(id);
        //静态化
        return generateHtml(model, template);
    }

    private String generateHtml(Map model, String template) {
        /**
         * 页面静态化
         */
        try {
            Configuration configuration = new Configuration(Configuration.getVersion());//生成配置类
            StringTemplateLoader loader = new StringTemplateLoader();//模板加载器
            loader.putTemplate("template",template);
            configuration.setTemplateLoader(loader);//配置模板加载器
            Template template1 = configuration.getTemplate("template");//获取模板
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template1, model);
            if (StringUtils.isEmpty(html)){
                ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
            }
            return html;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getTemplate(String id) {
        /**
         * 获取模板
         */
        CmsPage cmsPage = this.findById(id);
        String templateId = cmsPage.getTemplateId();
        if (StringUtils.isEmpty(templateId)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        if (optional.isPresent()){
            CmsTemplate cmsTemplate = optional.get();
            String templateFileId = cmsTemplate.getTemplateFileId();//模板文件的id
            GridFSFile gridFsFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));//获取模板文件
            GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(gridFsFile.getObjectId());//打开下载流
            GridFsResource gridFsResource = new GridFsResource(gridFsFile,downloadStream);//把模板文件下载到资源文件中
            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "UTF-8");
                return content;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Map getModelByPageId(String id) {
        /**
         * 获取模型数据
         */
        CmsPage cmsPage = this.findById(id);//查询出页面信息
        String dataUrl = cmsPage.getDataUrl();//查询出dataUrl
        if (StringUtils.isEmpty(dataUrl)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        ResponseEntity<Map> entity = restTemplate.getForEntity(dataUrl, Map.class);//使用restTemplate发送请求，查询页面配置对象
        if (entity==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        return entity.getBody();//取出模型数据
    }

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    public ResponseResult postPage(String pageId){
        //执行静态化
        String pageHtml = this.getPageHtml(pageId);
        if (StringUtils.isEmpty(pageHtml)){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //储存静态化页面
        CmsPage cmsPage = saveHtml(pageId, pageHtml);
        //发送消息
        sendPostPage(pageId);
        return new ResponseResult(CommonCode.SUCCESS);

    }
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 页面发布
     * @param pageId
     */
    public void sendPostPage(String pageId){
        CmsPage cmsPage = this.findById(pageId);
        String siteId = cmsPage.getSiteId();
        Map<String,String> msgMap = new HashMap<>();
        msgMap.put("pageId",pageId);
        String msg = JSON.toJSONString(msgMap);
        //发布消息
        rabbitTemplate.convertAndSend(RabbitmqConfig.EX_ROUTING_CMS_POSTAGE,siteId,msg);
    }

    /**
     * 将静态化文件存储到GridFS服务器
     * @param id
     * @param content
     * @return
     */
    private CmsPage saveHtml(String id,String content){
        //存储文件
        InputStream inputStream = IOUtils.toInputStream(content);
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (!optional.isPresent()){
            throw new CustomExeception(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        CmsPage cmsPage = optional.get();
        ObjectId objectId = gridFsTemplate.store(inputStream, cmsPage.getPageName());
        String fileId = objectId.toString();
        //将cmsPage更新
        cmsPage.setHtmlFileId(fileId);
        cmsPageRepository.save(cmsPage);
        return cmsPage;
    }


}
