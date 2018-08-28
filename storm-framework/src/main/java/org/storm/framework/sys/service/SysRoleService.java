package org.storm.framework.sys.service;

import org.storm.framework.base.service.BaseService;
import org.storm.framework.sys.model.SysRole;

import java.util.List;

public interface SysRoleService extends BaseService<SysRole> {

    List<SysRole> getUserRoles(Long userId);

    void saveMenusForRole(long roleId, List<Long> menuIds);

    SysRole getRoleByCode(String code);

    List<SysRole> getAllByValid();
}
