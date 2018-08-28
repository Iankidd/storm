package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysMenuMapper;
import org.storm.framework.sys.mapper.SysRefRoleMenuMapper;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysRole;
import org.storm.framework.sys.service.SysMenuService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("sysMenuService")
public class SysMenuServiceImpl extends BaseServiceImpl<SysMenu> implements SysMenuService {

    @Autowired
    protected SysMenuMapper sysMenuMapper;
    @Autowired
    private SysRefRoleMenuMapper sysRefRoleMenuMapper;
    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public BaseMapper<SysMenu> getBaseMapper() {
        return sysMenuMapper;
    }

    @Override
    public Class<SysMenuMapper> getMpClass() {
        return SysMenuMapper.class;
    }

    @Override
    public List<SysMenu> getListByRoleIds(List<Long> roleIds) {
        List<SysMenu> sysMenuList = new ArrayList<>();
        Map<String, Object> paramMap = null;
        List<Long> menuIds = sysRefRoleMenuMapper.getIdsByRoleIds(roleIds);
        if (menuIds != null && menuIds.size() > 0) {
            paramMap = new HashMap<>();
            paramMap.put("ids", menuIds);
            sysMenuList = sysMenuService.getList(paramMap);
        }
        return sysMenuList;
    }

    @Override
    public List<SysMenu> getListByRoles(List<SysRole> roles) {
        List<Long> roleIds = new ArrayList<>();
        for (SysRole role : roles) {
            if (role.getId() != null && role.getId() > 0) {
                roleIds.add(role.getId());
            }
        }
        return getListByRoleIds(roleIds);
    }

    @Override
    public void deleteById(long id) {
        List<SysMenu> list = new ArrayList<>();
        list = sysMenuMapper.getListByParentId(id);//找出子节点
        if (list != null && list.size() > 0) {
            for (SysMenu e : list) {
                deleteById(e.getId());
            }
        }
        sysMenuMapper.deleteById(id);
    }
}
