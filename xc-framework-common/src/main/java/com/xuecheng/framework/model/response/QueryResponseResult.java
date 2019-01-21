package com.xuecheng.framework.model.response;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class QueryResponseResult<T> extends ResponseResult {

    QueryResult queryResult = new QueryResult();

    public QueryResponseResult(ResultCode resultCode,QueryResult queryResult){
        super(resultCode);
        //System.out.println("默认构造执行");
        this.queryResult = queryResult;
    }

    public QueryResponseResult(ResultCode resultCode,List<T> list,long total){
        super(resultCode);
        //System.out.println("自定义构造执行");
        queryResult.setList(list);
        queryResult.setTotal(total);
    }
}
