package org.storm.framework.base.exception;

public class BusinessException extends Exception {

    private String moduleName;
    private String level;
    private String errorCode;
    private String errorCause;

    public BusinessException(String moduleName, String level, String errorCode, String errorCause) {
        super();
        this.moduleName = moduleName;
        this.level = level;
        this.errorCode = errorCode;
        this.errorCause = errorCause;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public BusinessException() {
        super();
    }

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
