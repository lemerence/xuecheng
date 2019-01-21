package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 0:50 2019/1/14
 */
public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    /**
     * 根据课程id和父节点查询根节点
     * @param courseId
     * @param parentId
     * @return
     */
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
