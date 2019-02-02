package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 10:47 2019/2/1
 */
public interface XcUserRepository extends JpaRepository<XcUser,String> {

    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    XcUser findXcUserByUsername(String username);
}

