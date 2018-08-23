package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysServiceErrorLog extends Entity {
    private static final long serialVersionUID = 1L;

    protected String sysName;
    protected String moduleName;
    protected Long taskId;
    protected String level;
    protected String errorCode;
    protected String errorCause;
    protected String params;


    public SysServiceErrorLog() {

    }

    public String getSysName() {
        return this.sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCause() {
        return this.errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public String getParams() {
        return this.params;
    }

    public void setParams(String params) {
        this.params = params;
    }


}
