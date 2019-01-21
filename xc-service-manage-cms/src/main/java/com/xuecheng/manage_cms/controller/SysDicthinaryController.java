package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.config.cms.SysDicthinaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDicthinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 13:29 2019/1/15
 */
@RestController
@RequestMapping("/system")
public class SysDicthinaryController implements SysDicthinaryControllerApi {

    @Autowired
    SysDicthinaryService sysDicthinaryService;

    @Override
    @GetMapping("/courseGrade/{dType}")
    public SysDictionary querryCourseGrade(@PathVariable String dType) {
        return sysDicthinaryService.querryCourseGrade(dType);
    }
}
