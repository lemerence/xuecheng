package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.SysDicthinaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 13:38 2019/1/15
 */
@Service
public class SysDicthinaryService {


    @Autowired
    SysDicthinaryRepository sysDicthinaryRepository;
    public SysDictionary querryCourseGrade(String dType){
        if (StringUtils.isEmpty(dType)){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return sysDicthinaryRepository.findByDType(dType);
    }
}
