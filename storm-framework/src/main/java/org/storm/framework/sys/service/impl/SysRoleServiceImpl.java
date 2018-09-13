package org.storm.framework.sys.service.impl;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysRefRoleMenuMapper;
import org.storm.framework.sys.mapper.SysRoleMapper;
import org.storm.framework.sys.model.SysRefRoleMenu;
import org.storm.framework.sys.model.SysRole;
import org.storm.framework.sys.service.SysRoleService;

import java.util.*;

@Service("sysRoleService")
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole> implements SysRoleService {

    @Autowired
    protected SysRoleMapper sysRoleMapper;

    @Autowired
    private SysRefRoleMenuMapper sysRefRoleMenuMapper;

    @Override
    public BaseMapper<SysRole> getBaseMapper() {
        return sysRoleMapper;
    }

    @Override
    public Class<SysRoleMapper> getMpClass() {
        return SysRoleMapper.class;
    }

    @Override
    public List<SysRole> getUserRoles(Long userId) {
        return sysRoleMapper.getUserRoles(userId);
    }

    @Override
    public void saveMenusForRole(long roleId, List<Long> menuIds) {
        SysRefRoleMenu SysRefRoleMenu = null;
        List<SysRefRoleMenu> list = new ArrayList<>();
        if (menuIds != null) {
            for (Long resId : menuIds) {
                SysRefRoleMenu = new SysRefRoleMenu();
                SysRefRoleMenu.setSysRoleId(roleId);
                SysRefRoleMenu.setSysMenuId(resId);
                list.add(SysRefRoleMenu);
            }
        }
        long t1 = System.currentTimeMillis();
        sysRefRoleMenuMapper.deleteByRoleId(roleId);
        for (SysRefRoleMenu te : list) {
            sysRefRoleMenuMapper.save(te);
        }
        long t2 = System.currentTimeMillis();
        logger.debug("debug at " + this.getClass().getName() + "saveMenusForRole 花费：" + (t2 - t1) / 1000 + "s");
    }


    @Override
    public void deleteByIds(String ids) {
        StringTokenizer token = new StringTokenizer(ids, ",");
        while (token.hasMoreTokens()) {
            String x = token.nextToken();
            this.deleteById(Long.parseLong(x));
        }
    }

    @Override
    public void deleteById(long id) {
        this.getBaseMapper().deleteById(id);
        sysRefRoleMenuMapper.deleteByRoleId(id);
    }

    @Override
    public SysRole getRoleByCode(String code) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("code", code);
        List<SysRole> list = this.getBaseMapper().getList(paramMap);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<SysRole> getAllByValid() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("status", 0);
        return this.getBaseMapper().getList(paramMap);
    }
}
