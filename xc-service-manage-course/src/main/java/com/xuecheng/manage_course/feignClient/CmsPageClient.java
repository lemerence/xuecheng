package com.xuecheng.manage_course.feignClient;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 11:02 2019/1/20
 */
@FeignClient("XC-SERVICE-MANAGE-CMS")//声明此类是Feign
public interface CmsPageClient {

    @GetMapping("/cms/page/get/{id}")
    public CmsPage findCmsPageById(@PathVariable String id);

}
