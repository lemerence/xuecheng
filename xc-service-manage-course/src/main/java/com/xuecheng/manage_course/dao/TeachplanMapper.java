package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 22:53 2019/1/13
 */
@Mapper
public interface TeachplanMapper {
    /**
     * 根据课程id查询课程计划（树结构）
     * @param courseId
     * @return
     */
    public TeachplanNode selectList(String courseId);

}
