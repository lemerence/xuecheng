package com.xuecheng.manage_cms.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 15:51 2019/1/3
 */

@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    PageService pageService;

    @RequestMapping(value="/cms/preview/{pageId}",method = RequestMethod.GET)
    public void preview(@PathVariable("pageId") String pageId) throws IOException {
        String pageHtml = pageService.getPageHtml(pageId);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(pageHtml.getBytes("UTF-8"));
    }

}
