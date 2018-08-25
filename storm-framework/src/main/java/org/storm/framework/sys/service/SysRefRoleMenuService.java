package org.storm.framework.sys.service;

import org.storm.framework.base.service.BaseService;
import org.storm.framework.sys.model.SysRefRoleMenu;

import java.util.List;

public interface SysRefRoleMenuService extends BaseService<SysRefRoleMenu> {

    List<Long> getIdsByRoleId(long roleId);

    List<Long> getIdsByRoleIds(List<Long> roleIds);
}
