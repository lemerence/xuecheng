package com.xuecheng.manage_course.controller;

import com.xuecheng.api.config.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 0:14 2019/1/14
 */
@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    CourseService courseService;

    @Override
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @Override
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    @PostMapping("list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") Integer page,@PathVariable("size") Integer size, CourseListRequest courseListRequest) {
        return courseService.findCourseList(page,size,courseListRequest);
    }

    @Override
    @PostMapping("/base/add")
    public ResponseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    @Override
    @GetMapping("/market/querryOne")
    public CourseMarket querryCourseMakeerById(String id) {
        return courseService.querryCourseMarketById(id);
    }

    @Override
    @PutMapping("/marker/update")
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {
        return courseService.updateCourseMarket(id,courseMarket);
    }

    /**
     * 文件上传成功后保存
     * @param courseId
     * @param fileId
     * @return
     */
    @Override
    @PostMapping("coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId,@RequestParam("fileId") String fileId) {
        return courseService.save(courseId,fileId);
    }
}
