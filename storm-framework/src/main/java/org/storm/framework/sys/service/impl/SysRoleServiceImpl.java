package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysRoleMapper;
import org.storm.framework.sys.model.SysRole;
import org.storm.framework.sys.service.SysRoleService;

@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements SysRoleService {

    @Autowired
    protected SysRoleMapper sysRoleMapper;

    @Override
    public BaseMapper<SysRole> getBaseMapper() {
        return sysRoleMapper;
    }

    @Override
    public Class<SysRoleMapper> getMpClass() {
        return SysRoleMapper.class;
    }

}
