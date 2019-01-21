package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 18:13 2018/12/28
 */
@Service
public class ConfigService {
    @Autowired
    CmsConfigRepository cmsConfigRepository;

    public CmsConfig getModel(String id){
        Optional<CmsConfig> optional = cmsConfigRepository.findById(id);
        if (optional.isPresent()){
            return optional.get();
        }
        return null;
    }
}
