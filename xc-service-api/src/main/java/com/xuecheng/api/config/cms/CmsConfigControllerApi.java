package com.xuecheng.api.config.cms;

import com.xuecheng.framework.domain.cms.CmsConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 18:02 2018/12/28
 */
@Api(value="cms配置管理接口",description = "cms配置管理接口，提供数据模型的管理、查询接口")
public interface CmsConfigControllerApi {
    @ApiOperation("获取模版数据")
    public CmsConfig getModel(String id);

}
