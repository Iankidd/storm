package org.storm.framework.sys.service;

import org.storm.framework.base.service.BaseService;
import org.storm.framework.sys.model.SysUser;

import java.util.List;

public interface SysUserService extends BaseService<SysUser> {

    SysUser checkLogin(String userName, String pwd);

    SysUser getUserByCode(String code);

    void setRolesForUser(long userId, String roleIds);

    List<SysUser> getActivityList();
}
