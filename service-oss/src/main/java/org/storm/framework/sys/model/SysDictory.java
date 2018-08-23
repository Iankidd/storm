package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysDictory extends Entity {
    private static final long serialVersionUID = 1L;

    protected Long parentId;
    protected String code;
    protected String name;
    protected String dictoryValue;
    protected byte status;
    protected int sort;


    public SysDictory() {

    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
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

    public String getDictoryValue() {
        return this.dictoryValue;
    }

    public void setDictoryValue(String dictoryValue) {
        this.dictoryValue = dictoryValue;
    }

    public byte getStatus() {
        return this.status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }


}
