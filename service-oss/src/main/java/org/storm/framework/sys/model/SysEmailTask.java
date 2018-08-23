package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

import java.util.Date;

public class SysEmailTask extends Entity {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected String recipients;
    protected String content;
    protected byte status;
    protected Date sendDatetime;


    public SysEmailTask() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRecipients() {
        return this.recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
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

    public Date getSendDatetime() {
        return this.sendDatetime;
    }

    public void setSendDatetime(Date sendDatetime) {
        this.sendDatetime = sendDatetime;
    }


}
