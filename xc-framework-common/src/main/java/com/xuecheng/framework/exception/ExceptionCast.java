package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * 异常抛出类，便于集中管理异常。。。
 * @Author lemerence
 * @Version 1.0
 * @Date 15:44 2019/1/2
 */
public class ExceptionCast {

    public static void cast(ResultCode resultCode){
        throw new CustomExeception(resultCode);
    }

}
