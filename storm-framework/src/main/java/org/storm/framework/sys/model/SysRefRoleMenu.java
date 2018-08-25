package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysRefRoleMenu extends Entity {
    private static final long serialVersionUID = 1L;

    protected Long sysMenuId;
    protected Long sysRoleId;


    public SysRefRoleMenu() {

    }

    public Long getSysMenuId() {
        return this.sysMenuId;
    }

    public void setSysMenuId(Long sysMenuId) {
        this.sysMenuId = sysMenuId;
    }

    public Long getSysRoleId() {
        return this.sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }


}
