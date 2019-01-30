package com.xuecheng.api.config.course;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 22:48 2019/1/13
 */
@Api(value="课程计划接口",description = "提供课程计划的增删改查操作")
public interface CourseControllerApi {
    @ApiOperation("课程计划查询")
    public TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("查询我的课程列表")
    public QueryResponseResult<CourseInfo> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest);

    @ApiOperation("添加课程")
    public ResponseResult addCourseBase(CourseBase courseBase);

    @ApiOperation("课程营销回填")
    public CourseMarket querryCourseMakeerById(String id);

    @ApiOperation("课程营销修改")
    public ResponseResult updateCourseMarket(String id,CourseMarket courseMarket);

    @ApiOperation("保存课程图片")
    public ResponseResult addCoursePic(String courseId,String fileId);

    @ApiOperation("课程视图查询")
    public CourseView courseView(String courseId);

    @ApiOperation("导入课程到索引库")
    public ResponseResult courseES(String id);
}
