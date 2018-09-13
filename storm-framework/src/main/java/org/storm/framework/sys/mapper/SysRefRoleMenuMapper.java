package org.storm.framework.sys.mapper;

import org.springframework.stereotype.Component;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.sys.model.SysRefRoleMenu;

import java.util.List;

@Component
public interface SysRefRoleMenuMapper extends BaseMapper<SysRefRoleMenu> {

    List<Long> getIdsByRoleIds(List<Long> roleIds);

    void deleteByRoleId(long roleId);

    void deleteByMenuId(long menuId);
}
