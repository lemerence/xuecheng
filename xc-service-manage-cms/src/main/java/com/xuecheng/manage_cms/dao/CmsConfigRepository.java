package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 18:08 2018/12/28
 */
public interface CmsConfigRepository extends MongoRepository<CmsConfig,String> {
}
