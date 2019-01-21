package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Description: 自定义异常类
 * @Author: lemerence
 * @Date: Create in 1:10 2018/12/28
 */
public class CustomExeception extends RuntimeException {

    ResultCode resultCode;

    public CustomExeception(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
