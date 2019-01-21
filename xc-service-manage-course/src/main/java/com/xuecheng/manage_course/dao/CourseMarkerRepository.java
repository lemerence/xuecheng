package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseMarket;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 16:03 2019/1/15
 */
public interface CourseMarkerRepository extends JpaRepository<CourseMarket,String> {
}
