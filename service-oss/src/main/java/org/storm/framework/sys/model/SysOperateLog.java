package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysOperateLog extends Entity {
    private static final long serialVersionUID = 1L;

    protected String modelName;
    protected String userAgent;
    protected String accessUrl;
    protected String ip;
    protected String params;


    public SysOperateLog() {

    }

    public String getModelName() {
        return this.modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getUserAgent() {
        return this.userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getAccessUrl() {
        return this.accessUrl;
    }

    public void setAccessUrl(String accessUrl) {
        this.accessUrl = accessUrl;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }


}
