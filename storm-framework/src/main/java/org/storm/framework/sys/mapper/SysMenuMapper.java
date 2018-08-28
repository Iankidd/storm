package org.storm.framework.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.sys.model.SysMenu;

import java.util.List;

@Component
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> getListByParentId(@Param("parentId") long parentId);
}
