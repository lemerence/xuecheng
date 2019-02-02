package com.xuecheng.auth.feignClient;

import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 11:44 2019/2/1
 */

@FeignClient(XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient {
    @GetMapping("/ucenter/getXcUserExt")
    public XcUserExt getXcUserExt(@RequestParam("username") String username);
}
