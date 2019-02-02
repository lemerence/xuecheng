package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description: cms页面管理接口
 * @Author: lemerence
 * @Date: Create in 14:27 2018/12/25
 */
@Api(value = "cms页面管理接口",description="cms页面管理接口，提供页面的增、删、改、查")//value的值不显示
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    PageService pageService;

    /**
     * 查询页面
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @Override
    @GetMapping("/list/{page}/{size}")//get方式请求，相当于requestMaping
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return pageService.findList(page,size,queryPageRequest);
    }

    /**
     * 添加页面
     * @param cmsPage
     * @return
     */
    @PostMapping("/add")
    @Override
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    /**
     * 删除页面
     * @param pageId
     * @return
     */
    @DeleteMapping("del/{pageId}")
    @Override
    public ResponseResult del(@PathVariable String pageId) {
        return pageService.del(pageId);
    }

    /**
     * 页面发布
     * @param pageId
     * @return
     */
    @PostMapping("postPage/{pageId}")
    @Override
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return pageService.postPage(pageId);
    }

    /**
     * 根据id查询页面
     * @param pageId
     * @return
     */
    @GetMapping("/get/{pageId}")
    @Override
    public CmsPage get(@PathVariable String pageId) {
        return pageService.findById(pageId);
    }

    /**
     * 修改页面
     * @param cmsPage
     * @return
     */
    @PutMapping("/update")
    @Override
    public CmsPageResult update(@RequestBody CmsPage cmsPage) {
        return pageService.update(cmsPage);
    }

}
