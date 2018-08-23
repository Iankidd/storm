package org.storm.framework.sys.model;

import org.storm.framework.base.model.Entity;

public class SysMenu extends Entity {
    private static final long serialVersionUID = 1L;

    protected String name;
    protected String module;
    protected String url;
    protected String keyword;
    protected String icon;
    protected byte type;
    protected byte isLeaf;
    protected byte isActive;
    protected Long parentId;
    protected int sort;


    public SysMenu() {

    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return this.module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public byte getType() {
        return this.type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getIsLeaf() {
        return this.isLeaf;
    }

    public void setIsLeaf(byte isLeaf) {
        this.isLeaf = isLeaf;
    }

    public byte getIsActive() {
        return this.isActive;
    }

    public void setIsActive(byte isActive) {
        this.isActive = isActive;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public int getSort() {
        return this.sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }


}
