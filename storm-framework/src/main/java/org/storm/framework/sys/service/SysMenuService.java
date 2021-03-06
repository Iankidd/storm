package org.storm.framework.sys.service;

import org.storm.framework.base.service.BaseService;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysRole;

import java.util.List;
import java.util.Set;

public interface SysMenuService extends BaseService<SysMenu> {

    List<SysMenu> getListByRoleIds(List<Long> roleIds);

    List<SysMenu> getListByRoles(List<SysRole> roles);

    Set<String> getPermissionSet(List<SysMenu> menuList);
}
