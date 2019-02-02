package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsSearchController;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 15:52 2019/1/28
 */
@RestController
@RequestMapping("/search")
public class SearchController implements EsSearchController {

    @Autowired
    SearchService searchService;

    @Override
    @PostMapping("/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(Integer page, Integer size, CourseSearchParam courseSearchParam) {
        return searchService.list(page,size,courseSearchParam);
    }
}
