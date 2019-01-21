package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

/**
 * @Description:
 * @Author: lemerence
 * @Date: Create in 18:01 2018/12/25
 */
@RunWith(SpringRunner.class)
@SpringBootTest//(classes = ManageCmsApplication.class)    如果该类与启动类不在统一目录，需要指定启动类
public class CmsPageRepositoryTest {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Test
    public void testFindByPage(){
        Pageable pageable = PageRequest.of(0,10);
        Page<CmsPage> all = cmsPageRepository.findAll(pageable);
        System.out.println(all);
    }

    @Test
    public void testInsert(){
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面测试页面测试页面");
        cmsPageRepository.save(cmsPage);
        System.out.println(cmsPage);
    }

    @Test
    public void testUpdate(){
        Optional<CmsPage> optional = cmsPageRepository.findById("5c220ab8a235d91a6488e067");
        if (optional.isPresent()){
            CmsPage cmsPage = optional.get();
            cmsPage.setPageName("修改测试页面修改测试页面修改测试页面");
            cmsPageRepository.save(cmsPage);
        }
    }
    @Test
    public void testFindAll(){
        Pageable pagable = new PageRequest(0,10);

        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("课程预览页面");
        cmsPage.setTemplateId("5ad9a24d68db5239b8fef199");
       // cmsPage.setPageCreateTime(new Date("2018-05-05T11:24:03.619+0000"));

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<CmsPage> example = Example.of(cmsPage,matcher);

        Page<CmsPage> all = cmsPageRepository.findAll(example, pagable);
        List<CmsPage> content = all.getContent();
        for (CmsPage page : content) {
            System.out.println(page);
        }
    }
}
