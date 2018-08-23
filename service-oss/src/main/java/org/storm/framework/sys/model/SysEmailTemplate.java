package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysEmailTemplate extends Entity {
    private static final long serialVersionUID = 1L;

    protected byte type;
    protected String content;
    protected byte status;


    public SysEmailTemplate() {

    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }


}
