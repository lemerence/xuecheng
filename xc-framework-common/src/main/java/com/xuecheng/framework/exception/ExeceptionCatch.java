package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 1:14 2018/12/28
 */
@ControllerAdvice//捕获异常
public class ExeceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExeceptionCatch.class);

    @ExceptionHandler(CustomExeception.class)//处理那一类异常
    @ResponseBody
    public ResponseResult CustomException(CustomExeception e){

        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);

        ResultCode resultCode = e.getResultCode();
        return new ResponseResult(resultCode);
    }

    private static ImmutableMap<Class<? extends Throwable>,ResultCode> EXCEPTIONS;//ImmutableMap 线程安全，一旦复制固定不变
    protected static ImmutableMap.Builder<Class<? extends Throwable>,ResultCode> builder = ImmutableMap.builder();

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseResult exception(Exception e){

        LOGGER.error("catch exception : {}\r\nexception: ",e.getMessage(),e);

        if (EXCEPTIONS==null){
            EXCEPTIONS = builder.build();
        }
        ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if(resultCode !=null){
            return new ResponseResult(resultCode);
        }else{
            //返回99999异常
            return new ResponseResult(CommonCode.SERVER_ERROR);
        }
    }
    static {
        //定义异常类型所对应的错误代码
        builder.put(HttpMessageNotReadableException.class,CommonCode.INVALID_PARAM);
        //builder.put(AccessDeniedException.class,CommonCode.UNAUTHORISE);
    }
}
