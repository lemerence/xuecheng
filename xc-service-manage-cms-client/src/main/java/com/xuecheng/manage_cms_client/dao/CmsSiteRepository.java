package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsSite;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 21:43 2019/1/11
 */
public interface CmsSiteRepository extends MongoRepository<CmsSite,String> {
}
