package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.config.cms.CmsConfigControllerApi;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 页面信息
 * @Author: lemerence
 * @Date: Create in 18:16 2018/12/28
 */
@RestController
@RequestMapping("/cms/config")
public class CmsConfigController implements CmsConfigControllerApi {

    @Autowired
    ConfigService configService;

    @GetMapping("/getModel/{id}")
    @Override
    public CmsConfig getModel(@PathVariable("id") String id) {
        return configService.getModel(id);
    }
}
