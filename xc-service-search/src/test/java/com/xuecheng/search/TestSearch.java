package com.xuecheng.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSearch {

    @Autowired
    RestHighLevelClient client;

    @Autowired
    RestClient restClient;

    //创建索引库
    @Test
    public void testSearch() throws IOException {
        //创建搜索请求
        SearchRequest searchRequest = new SearchRequest("xc_course");
        //设置类型
        searchRequest.types("doc");
        //设置搜索源
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置搜索类型    Operator.OR表示只要有一个词匹配就得分    minimumShouldMatch("80%")表示：java开发，两个词，2*80%=1.6向上取整1即只要有一个词匹配就得分
        //searchSourceBuilder.query(QueryBuilders.matchAllQuery());   //搜索全部
        //searchSourceBuilder.query(QueryBuilders.matchQuery("description","java开发").operator(Operator.OR));
        //searchSourceBuilder.query(QueryBuilders.matchQuery("description","java开发").minimumShouldMatch("80%"));
        //searchSourceBuilder.query(QueryBuilders.matchQuery("description","java开发").minimumShouldMatch("100%"));//两个词都匹配才得分
        //searchSourceBuilder.query(QueryBuilders.termQuery("studymodel","201001");  //精确匹配
        //searchSourceBuilder.query(QueryBuilders.multiMatchQuery("开发","name","description"));//可以匹配多个字段

        TermQueryBuilder termQuery = QueryBuilders.termQuery("studymodel", "201002");
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("开发", "name", "description");
        //布尔
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(multiMatchQueryBuilder);//must
        boolQueryBuilder.should(termQuery);//shoud    should_not
        //过滤
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("price").gte(60).lte(100));
        searchSourceBuilder.query(boolQueryBuilder);
        //排序
        searchSourceBuilder.sort("price", SortOrder.DESC);
        //设置搜索字段和过滤字段
        searchSourceBuilder.fetchSource(new String[]{"name","description","studymodel"},new String[]{});
        //分页
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(2);
        //高亮显示
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<tag>");
        highlightBuilder.postTags("</tag>");
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        highlightBuilder.fields().add(new HighlightBuilder.Field("description"));
        searchSourceBuilder.highlighter(highlightBuilder);
        //搜索条件添加到搜索请求
        searchRequest.source(searchSourceBuilder);
        //发送请求
        SearchResponse searchResponse = client.search(searchRequest);

        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String sourceAsString = hit.getSourceAsString();
            System.out.println(sourceAsString);
            //取出原文档的name
            String name = (String) sourceAsMap.get("name");
            String description = (String) sourceAsMap.get("description");
            //取出高亮name
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            if (highlightFields!=null){
                HighlightField hightName = highlightFields.get("name");
                HighlightField hightDescription = highlightFields.get("description");
                if (hightName!=null){
                    Text[] fragments = hightName.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text fragment : fragments) {
                        stringBuffer.append(fragment.toString());
                    }
                    name = stringBuffer.toString();
                }
                if (hightDescription!=null){
                    Text[] fragments = hightDescription.getFragments();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (Text fragment : fragments) {
                        stringBuffer.append(fragment.toString());
                    }
                    description = stringBuffer.toString();
                }
            }
            System.out.println(name);
            System.out.println(description);
        }
    }
}
