package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysUserLoginMapper;
import org.storm.framework.sys.model.SysUserLogin;
import org.storm.framework.sys.service.SysUserLoginService;

@Service("sysUserLoginService")
public class SysUserLoginServiceImpl extends BaseServiceImpl<SysUserLogin> implements SysUserLoginService {

    @Autowired
    protected SysUserLoginMapper sysUserLoginMapper;

    @Override
    public BaseMapper<SysUserLogin> getBaseMapper() {
        return sysUserLoginMapper;
    }

    @Override
    public Class<SysUserLoginMapper> getMpClass() {
        return SysUserLoginMapper.class;
    }

}
