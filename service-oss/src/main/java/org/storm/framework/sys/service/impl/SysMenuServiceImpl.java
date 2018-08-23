package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysMenuMapper;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.service.SysMenuService;

@Service("sysMenuService")
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {

    @Autowired
    protected SysMenuMapper sysMenuMapper;

    @Override
    public BaseMapper<SysMenu> getBaseMapper() {
        return sysMenuMapper;
    }

    @Override
    public Class<SysMenuMapper> getMpClass() {
        return SysMenuMapper.class;
    }

}
