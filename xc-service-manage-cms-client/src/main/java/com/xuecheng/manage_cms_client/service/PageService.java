package com.xuecheng.manage_cms_client.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;


/**
 * @Author lemerence
 * @Version 1.0
 * @Date 21:44 2019/1/11
 */
@Service
public class PageService {
    //记录日志
    private static final Logger LOGGER = LoggerFactory.getLogger(PageService.class);
    @Autowired
    GridFsTemplate gridFsTemplate;
    @Autowired
    GridFSBucket gridFSBucket;
    @Autowired
    CmsPageRepository cmsPageRepository;
    @Autowired
    CmsSiteRepository cmsSiteRepository;

    /**
     * 从gridFS下载页面html再保存到服务器指定路径
     * @param pageId
     */
    public void savePageToServerPath(String pageId){
        //查询cmsPage获取htmlFiledId
        CmsPage cmsPage = findCmsPageById(pageId);
        String htmlFileId = cmsPage.getHtmlFileId();
        //通过hrmlFiledId从GridFS获取文件流对象
        InputStream inputStream = getFileById(htmlFileId);
        if (inputStream == null){
            LOGGER.error("class: PageService, method: getFileById, Inputstream is null,htmlFileId",htmlFileId);
            return;
        }
        //获取文件存储的物理路径
        CmsSite cmsSite = findCmsSiteById(cmsPage.getSiteId());
        String sitePhysicalPath = cmsSite.getSitePhysicalPath();//站点物理路径
        String pagePath = sitePhysicalPath+cmsPage.getPagePhysicalPath()+cmsPage.getPageName();
        //将文件存储到物理路径
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(new File(pagePath));
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //获取文件流对象
    public InputStream getFileById(String id){
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        ObjectId objectId = gridFSFile.getObjectId();
        GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(objectId);
        GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
        try {
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //查询cmspage
    public CmsPage findCmsPageById(String id){
        Optional<CmsPage> optional = cmsPageRepository.findById(id);
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            return cmsPage;
        }
        return null;
    }
    //查询cmsSite
    public CmsSite findCmsSiteById(String id){
        Optional<CmsSite> optional = cmsSiteRepository.findById(id);
        if (optional.isPresent()){
            CmsSite cmsSite = optional.get();
            return cmsSite;
        }
        return null;
    }

}
