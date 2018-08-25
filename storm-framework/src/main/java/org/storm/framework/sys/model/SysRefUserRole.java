package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysRefUserRole extends Entity {
    private static final long serialVersionUID = 1L;

    protected Long sysUserId;
    protected Long sysRoleId;


    public SysRefUserRole() {

    }

    public Long getSysUserId() {
        return this.sysUserId;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getSysRoleId() {
        return this.sysRoleId;
    }

    public void setSysRoleId(Long sysRoleId) {
        this.sysRoleId = sysRoleId;
    }


}
