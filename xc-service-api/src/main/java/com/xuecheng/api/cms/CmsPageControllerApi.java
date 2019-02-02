package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 11:38 2018/12/25
 */
public interface CmsPageControllerApi {
    //页面查询
    @ApiOperation("分页查询页面列表")
    @GetMapping("/list/{page}/{size}")//get方式请求，相当于requestMaping
    @ApiImplicitParams({@ApiImplicitParam(name = "page",value = "当前页",required = true,paramType = "path",dataType = "int"),
            @ApiImplicitParam(name = "size",value = "每页条数",required = true,paramType = "path",dataType = "int")})
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);
    //新增
    public CmsPageResult add(CmsPage cmsPage);
    //根据id查询
    public CmsPage get(String pageId);
    //更新
    public CmsPageResult update(CmsPage cmsPage);
    //删除
    public ResponseResult del(String pageId);
    //页面发布
    @ApiOperation("页面发布")
    public ResponseResult post(String pageId);
}
