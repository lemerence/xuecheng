package com.xuecheng.api.ucenter;

import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 10:52 2019/2/1
 */
@Api(value = "用户中心",description = "用户中心接口")
public interface UcenterControllerApi {

    @ApiOperation("根据用户名查询用户扩展信息")
    public XcUserExt getXcUserExt(String username);
}
