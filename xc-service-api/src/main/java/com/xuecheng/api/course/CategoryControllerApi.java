package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 22:36 2019/1/14
 */
@Api(value = "课程分类",description = "课程分类的增删改查")
public interface CategoryControllerApi {

    @ApiOperation("多级分类查询")
    public List<CategoryNode> findList();

}
