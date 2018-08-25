package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysRefUserRoleMapper;
import org.storm.framework.sys.model.SysRefUserRole;
import org.storm.framework.sys.service.SysRefUserRoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysRefUserRoleService")
public class SysRefUserRoleServiceImpl extends BaseServiceImpl<SysRefUserRole> implements SysRefUserRoleService {

    @Autowired
    protected SysRefUserRoleMapper sysRefUserRoleMapper;

    @Override
    public BaseMapper<SysRefUserRole> getBaseMapper() {
        return sysRefUserRoleMapper;
    }

    @Override
    public Class<SysRefUserRoleMapper> getMpClass() {
        return SysRefUserRoleMapper.class;
    }

    @Override
    public List<Long> getRolesIdByUserId(Long userId) {
        List<Long> rolesId = sysRefUserRoleMapper.getRolesIdByUserId(userId);
        return rolesId;
    }

    @Override
    public List<SysRefUserRole> getListByRoleId(long roleId) {
        Map<String, Object> param = new HashMap<>();
        param.put("sysRoleId", roleId);
        List<SysRefUserRole> list = sysRefUserRoleMapper.getList(param);
        return list;
    }
}
