package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysUserMapper;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.service.SysUserService;

@Service("sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {

    @Autowired
    protected SysUserMapper sysUserMapper;

    @Override
    public BaseMapper<SysUser> getBaseMapper() {
        return sysUserMapper;
    }

    @Override
    public Class<SysUserMapper> getMpClass() {
        return SysUserMapper.class;
    }

}
