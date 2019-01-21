package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 22:24 2019/1/14
 */
@Mapper
public interface CategoryMapper {
    /**
     * 分类查询
     * 自链接查询多级
     * @return
     */
    List<CategoryNode> selectList();
}
