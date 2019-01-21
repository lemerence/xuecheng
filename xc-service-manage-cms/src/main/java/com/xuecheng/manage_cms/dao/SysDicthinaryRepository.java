package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 13:37 2019/1/15
 */
public interface SysDicthinaryRepository extends MongoRepository<SysDictionary,String> {
    SysDictionary findByDType(String dType);
}
