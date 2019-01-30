package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 15:53 2019/1/28
 */
@Service
public class SearchService {

    @Autowired
    RestHighLevelClient restHighLevelClient;
    @Autowired
    RestClient restClient;

    public QueryResponseResult<CoursePub> list(Integer page, Integer size, CourseSearchParam courseSearchParam) {

        SearchRequest searchRequest = new SearchRequest("xc_course");
        searchRequest.types("doc");
        //搜索源对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //过滤
        searchSourceBuilder.fetchSource(new String[]{"name","description"},new String[]{});
        //分页
        searchSourceBuilder.from((page-1)*size);
        searchSourceBuilder.size(size);
        //布尔搜索  多条件
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())){
            //多个字段匹配关键字
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(courseSearchParam.getKeyword(),"name","description");
            //设置匹配占比
            multiMatchQueryBuilder.minimumShouldMatch("70");
            //提升name字段的匹配等级
            multiMatchQueryBuilder.field("name",10);
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //结果集处理
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");

        }
        return null;

    }
}
