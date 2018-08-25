package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

import java.util.Date;

public class SysUserLogin extends Entity {
    private static final long serialVersionUID = 1L;

    protected Long userId;
    protected String userAgent;
    protected String ip;
    protected Date loginDatetime;


    public SysUserLogin() {

    }

    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Date getLoginDatetime() {
        return this.loginDatetime;
    }

    public void setLoginDatetime(Date loginDatetime) {
        this.loginDatetime = loginDatetime;
    }


}
