package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 17:58 2018/12/25
 */
public interface CmsPageRepository extends MongoRepository<CmsPage,String> {
//根据页面名称查询
    CmsPage findByPageName(String name);
//根据索引
    CmsPage findBySiteIdAndPageNameAndAndPageWebPath(String siteId,String pageName,String pageWebPath);
//根据id查询
    CmsPage findByPageId(String pageId);
}
