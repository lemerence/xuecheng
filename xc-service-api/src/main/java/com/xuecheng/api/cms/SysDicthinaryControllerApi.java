package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 13:16 2019/1/15
 */
@Api(value = "数据字典接口",description = "提供数据字典接口的管理、查询功能")
public interface SysDicthinaryControllerApi {
    @ApiOperation("课程等级字典")
    public SysDictionary querryCourseGrade(String dType);
}
