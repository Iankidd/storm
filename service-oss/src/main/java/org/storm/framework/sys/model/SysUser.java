package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysUser extends Entity {
    private static final long serialVersionUID = 1L;

    protected String code;
    protected String name;
    protected Long deptId;
    protected String email;
    protected String pwd;
    protected byte status;
    protected byte recvEmailFlag;
    protected Long createUser;


    public SysUser() {

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

    public Long getDeptId() {
        return this.deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public byte getRecvEmailFlag() {
        return this.recvEmailFlag;
    }

    public void setRecvEmailFlag(byte recvEmailFlag) {
        this.recvEmailFlag = recvEmailFlag;
    }

    public Long getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }


}
