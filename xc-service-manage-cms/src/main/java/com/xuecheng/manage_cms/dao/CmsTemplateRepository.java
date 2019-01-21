package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 13:54 2019/1/3
 */
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
