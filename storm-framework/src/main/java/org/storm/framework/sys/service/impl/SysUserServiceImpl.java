package org.storm.framework.sys.service.impl;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.sys.mapper.SysRefUserRoleMapper;
import org.storm.framework.sys.mapper.SysUserMapper;
import org.storm.framework.sys.model.SysRefUserRole;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.service.SysUserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Service("sysUserService")
public class SysUserServiceImpl extends BaseServiceImpl<SysUser> implements SysUserService {

    @Autowired
    protected SysUserMapper sysUserMapper;

    @Autowired
    protected SysRefUserRoleMapper sysRefUserRoleMapper;

    @Override
    public BaseMapper<SysUser> getBaseMapper() {
        return sysUserMapper;
    }

    @Override
    public Class<SysUserMapper> getMpClass() {
        return SysUserMapper.class;
    }

    @Override
    public SysUser checkLogin(String userName, String pwd) {
        SysUser user = null;
        try {

            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("code", userName);
            paramMap.put("pwd", pwd);
            paramMap.put("status", SysConstants.EStatus.Valid.ordinal());
            List<SysUser> list = sysUserMapper.getList(paramMap);
            if (list.size() > 0) {
                user = list.get(0);
            }
        } catch (Exception ex) {
            logger.error("error at " + this.getClass().getName() + ".checkLogin: " + ExceptionUtils.getFullStackTrace(ex));
        }
        return user;
    }

    @Override
    public SysUser getUserByCode(String code) {
        SysUser user = null;
        try {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("code", code);
            List<SysUser> list = sysUserMapper.getList(paramMap);
            if (list.size() > 0) {
                user = list.get(0);
            }
        } catch (Exception ex) {
            logger.error("error at " + this.getClass().getName() + ".getUserByCode: " + ExceptionUtils.getFullStackTrace(ex));
        }
        return user;
    }

    @Override
    public void setRolesForUser(long userId, String roleIds) {
        sysRefUserRoleMapper.deleteUserRoleByUserId(userId);
        SysRefUserRole sysRefUserRole = null;
        StringTokenizer token = new StringTokenizer(roleIds, ",");
        while (token.hasMoreTokens()) {
            String roleId = token.nextToken();
            sysRefUserRole = new SysRefUserRole();
            sysRefUserRole.setSysUserId(userId);
            sysRefUserRole.setSysRoleId(Long.parseLong(roleId));
            sysRefUserRoleMapper.save(sysRefUserRole);
        }
    }

    @Override
    public List<SysUser> getActivityList() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", SysConstants.EStatus.Valid.ordinal());
        List<SysUser> list = sysUserMapper.getList(paramMap);
        return list;
    }
}
