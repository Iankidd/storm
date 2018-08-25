package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysRefRoleMenuMapper;
import org.storm.framework.sys.model.SysRefRoleMenu;
import org.storm.framework.sys.service.SysRefRoleMenuService;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<Long> getIdsByRoleId(long roleId) {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        List<Long> ids = sysRefRoleMenuMapper.getIdsByRoleIds(roleIds);
        return ids;
    }

    @Override
    public List<Long> getIdsByRoleIds(List<Long> roleIds) {
        List<Long> ids = sysRefRoleMenuMapper.getIdsByRoleIds(roleIds);
        return ids;
    }
}
