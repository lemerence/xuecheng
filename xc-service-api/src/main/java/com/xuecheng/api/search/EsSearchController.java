package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 15:48 2019/1/28
 */
@Api(value = "es课程搜索",description = "课程搜索")
public interface EsSearchController {

    @ApiOperation("课程搜索")
    public QueryResponseResult<CoursePub> list(Integer page, Integer size, CourseSearchParam courseSearchParam);
}
