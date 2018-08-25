package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysRole extends Entity {
    private static final long serialVersionUID = 1L;

    protected String code;
    protected String name;
    protected byte status;
    protected Long createUser;


    public SysRole() {

    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public Long getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }


}
