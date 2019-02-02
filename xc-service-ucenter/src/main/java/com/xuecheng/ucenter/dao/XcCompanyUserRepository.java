package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 10:56 2019/2/1
 */
public interface XcCompanyUserRepository extends JpaRepository<XcCompanyUser,String> {
    XcCompanyUser findByUserId(String userId);
}
