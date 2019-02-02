package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author lemerence
 * @Version 1.0
 * @Date 22:14 2019/2/1
 */
@Mapper
public interface XcMenuMapper {

    List<XcMenu> selectPermissionByUserId(String username);

}
