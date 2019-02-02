package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 11:18 2019/2/1
 */
@Service
public class UserService {

    @Autowired
    XcUserRepository xcUserRepository;
    @Autowired
    XcCompanyUserRepository xcCompanyUserRepository;

    public XcUser findByUsername(String username){
        return xcUserRepository.findXcUserByUsername(username);
    }

    /**
     * 根据用户名查询用户扩展信息
     * @param username
     * @return
     */
    public XcUserExt getXcUserExt(String username){
        XcUser xcUser = findByUsername(username);
        if (xcUser==null){
            return null;
        }
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(xcUser.getId());
        XcUserExt xcUserExt = new XcUserExt();
        BeanUtils.copyProperties(xcUser,xcUserExt);
        xcUserExt.setCompanyId(xcCompanyUser.getCompanyId());
        return xcUserExt;
    }
}
