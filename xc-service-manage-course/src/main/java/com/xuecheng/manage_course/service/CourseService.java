package com.xuecheng.manage_course.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 0:11 2019/1/14
 */
@Service
public class CourseService {

    @Autowired
    TeachplanMapper teachplanMapper;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    CourseMapper courseMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(CourseService.class);

    /**
     * 查询课程计划
     *
     * @param courseId
     * @return
     */
    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.selectList(courseId);
    }

    /**
     * @param courseId
     * @return
     */
    public Teachplan getTeachplanRoot(String courseId) {
        //校验课程id
        Optional<CourseBase> optional = courseBaseRepository.findById(courseId);
        if (!optional.isPresent()) {
            return null;
        }
        CourseBase courseBase = optional.get();
        //取出课程计划根节点
        List<Teachplan> teachplanList = teachplanRepository.findByCourseidAndParentid(courseId, "0");
        if (teachplanList == null || teachplanList.size() == 0) {
            //添加跟节点
            Teachplan teachplan = new Teachplan();
            teachplan.setCourseid(courseId);
            teachplan.setPname(courseBase.getName());
            teachplan.setStatus("0");//未发布
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplanRepository.save(teachplan);
            return teachplan;
        }
        return teachplanList.get(0);
    }

    /**
     * 添加课程计划
     *
     * @param teachplan
     * @return
     */
    @Transactional
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //校验课程id和课程计划
        if (teachplan == null || StringUtils.isEmpty(teachplan.getCourseid()) || StringUtils.isEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        //取出课程id
        String courseid = teachplan.getCourseid();
        //取出父节点
        String parentId = teachplan.getParentid();
        //查出父节点信息
        Teachplan teachplanRoot = getTeachplanRoot(courseid);
        //设置父节点
        if (StringUtils.isEmpty(parentId)) {
            //父节点为空，则父节点为根节点
            parentId = teachplanRoot.getId();
        }
        Teachplan teachplan1 = new Teachplan();
        BeanUtils.copyProperties(teachplan, teachplan1);//拷贝属性
        teachplan1.setParentid(parentId);//设置父节点
        teachplan1.setStatus("0");//未发布
        if (teachplanRoot.getGrade().equals("1")) {
            teachplan1.setGrade("2");
        } else if (teachplanRoot.getGrade().equals("2")) {
            teachplan1.setGrade("3");
        }//设置级别
        teachplan.setCourseid(teachplanRoot.getCourseid());//设置课程id
        teachplanRepository.save(teachplan1);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 待条件分页查询课程
     *
     * @param page
     * @param size
     * @param courseListRequest
     * @return
     */
    public QueryResponseResult<CourseInfo> findCourseList(Integer page, Integer size, CourseListRequest courseListRequest) {
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 10;
        }
        if (courseListRequest == null) {
            courseListRequest = new CourseListRequest();
        }
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseList = courseMapper.findCourseList(courseListRequest);
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, courseList.getResult(), courseList.getTotal());
    }

    /**
     * 添加课程
     * @param courseBase
     * @return
     */
    public ResponseResult addCourseBase(CourseBase courseBase) {
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        if (StringUtils.isEmpty(courseBase.getName()) ||
            StringUtils.isEmpty(courseBase.getGrade()) ||
            StringUtils.isEmpty(courseBase.getStudymodel()) ||
            StringUtils.isEmpty(courseBase.getSt())) {
            ExceptionCast.cast(CommonCode.INVALID_PARAM);
        }
        courseBaseRepository.save(courseBase);
        return new ResponseResult(CommonCode.SUCCESS);
    }
    @Autowired
    CourseMarkerRepository courseMarkerRepository;

    /**
     * 根据id查询课程营销信息用于课程营销信息的修改回填
     * @param id
     * @return
     */
    public CourseMarket querryCourseMarketById(String id){
        Optional<CourseMarket> optional = courseMarkerRepository.findById(id);
        if (!optional.isPresent()){
            LOGGER.error("根据id找不到课程营销信息");
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return optional.get();
    }

    /**
     * 修改课程营销信息，如果课程营销信息不存在就新建课程营销信息
     * @param id
     * @param courseMarket
     * @return
     */
    public ResponseResult updateCourseMarket(String id, CourseMarket courseMarket) {

        CourseMarket courseMarket1 = querryCourseMarketById(id);
        if (courseMarket1 == null){
            ExceptionCast.cast(CommonCode.FAIL);
        }

        BeanUtils.copyProperties(courseMarket,courseMarket1);
        courseMarkerRepository.save(courseMarket1);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    @Autowired
    CoursePicRepository coursePicRepository;

    public ResponseResult save(String courseId, String fileId) {

        CoursePic coursePic = null;

        Optional<CoursePic> optional = coursePicRepository.findById(courseId);
        if (optional.isPresent()){
            coursePic = optional.get();
        }
        if (coursePic==null){
            coursePic=new CoursePic();
        }
        coursePic.setCourseid(courseId);
        coursePic.setPic(fileId);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    public CourseView getCourseView(String courseId) {
        CourseView courseView = new CourseView();
        Optional<CourseBase> baseOptional = courseBaseRepository.findById(courseId);
        if (baseOptional.isPresent()){
            courseView.setCourseBase(baseOptional.get());
        }
        Optional<CoursePic> picOptional = coursePicRepository.findById(courseId);
        if (picOptional.isPresent()){
            courseView.setCoursePic(picOptional.get());
        }
        Optional<CourseMarket> marketOptional = courseMarkerRepository.findById(courseId);
        if (marketOptional.isPresent()){
            courseView.setCourseMarket(marketOptional.get());
        }
        TeachplanNode teachplanNode = teachplanMapper.selectList(courseId);
        courseView.setTeachplanNode(teachplanNode);

        return courseView;
    }
}
