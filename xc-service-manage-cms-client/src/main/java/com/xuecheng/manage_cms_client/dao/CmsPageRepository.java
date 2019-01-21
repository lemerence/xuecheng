package com.xuecheng.manage_cms_client.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 16:25 2019/1/11
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
}
