package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 22:42 2019/1/14
 */
@Service
public class CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    public List<CategoryNode> findList(){
        List<CategoryNode> categoryNodes = categoryMapper.selectList();
        return categoryNodes;
    }
}
