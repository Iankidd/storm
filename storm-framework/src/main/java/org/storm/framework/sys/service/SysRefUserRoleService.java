package org.storm.framework.sys.service;

import org.storm.framework.base.service.BaseService;
import org.storm.framework.sys.model.SysRefUserRole;

import java.util.List;

public interface SysRefUserRoleService extends BaseService<SysRefUserRole> {

    List<Long> getRolesIdByUserId(Long userId);

    List<SysRefUserRole> getListByRoleId(long roleId);
}
