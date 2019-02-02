package com.xuecheng.manage_course.exception;

import com.xuecheng.framework.exception.ExeceptionCatch;
import com.xuecheng.framework.model.response.CommonCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 21:43 2019/2/1
 */
@ControllerAdvice//捕获异常
public class CourseExceptionCatch extends ExeceptionCatch {
    static {
        builder.put(AccessDeniedException.class, CommonCode.UNAUTHORISE);
    }
}
