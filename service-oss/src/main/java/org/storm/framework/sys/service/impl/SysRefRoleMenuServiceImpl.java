package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysRefRoleMenuMapper;
import org.storm.framework.sys.model.SysRefRoleMenu;
import org.storm.framework.sys.service.SysRefRoleMenuService;

@Service("sysRefRoleMenuService")
public class SysRefRoleMenuServiceImpl extends BaseServiceImpl<SysRefRoleMenu> implements SysRefRoleMenuService {

    @Autowired
    protected SysRefRoleMenuMapper sysRefRoleMenuMapper;

    @Override
    public BaseMapper<SysRefRoleMenu> getBaseMapper() {
        return sysRefRoleMenuMapper;
    }

    @Override
    public Class<SysRefRoleMenuMapper> getMpClass() {
        return SysRefRoleMenuMapper.class;
    }

}
