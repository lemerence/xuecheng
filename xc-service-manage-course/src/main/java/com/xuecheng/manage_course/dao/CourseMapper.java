package com.xuecheng.manage_course.dao;

import com.github.pagehelper.Page;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created by Administrator.
 */
@Mapper
public interface CourseMapper {

   CourseBase findCourseBaseById(String id);

   /**
    * 分页查询
    * @return
    */
   Page<CourseBase> findCourseListPage();

   /**
    * 待条件分页查询
    * @param courseListRequest
    * @return
    */
   Page<CourseInfo> findCourseList(CourseListRequest courseListRequest);
}
