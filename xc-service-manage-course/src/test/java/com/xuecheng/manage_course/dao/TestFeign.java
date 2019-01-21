package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_course.feignClient.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 21:54 2019/1/19
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeign {

    @Autowired
    CmsPageClient cmsPageClient;

    @Test
    public void test() {
        CmsPage cmsPage = cmsPageClient.findCmsPageById("5a7be667d019f14d90a1fb1c");
        System.out.println(cmsPage);
    }


}
