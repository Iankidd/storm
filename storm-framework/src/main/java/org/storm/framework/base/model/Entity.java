package org.storm.framework.base.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体对象的基类，所有实体类需继承该类
 */
public class Entity implements Serializable {

    // 把日志记录到slf4j中输出
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected Long id;
    protected Date createDatetime;
    protected Date modifyDatetime;
    protected Long createUserId;
    protected String createUserName;
    protected Long modifyUserId;
    protected String modifyUserName;
    protected String memo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public Date getModifyDatetime() {
        return modifyDatetime;
    }

    public void setModifyDatetime(Date modifyDatetime) {
        this.modifyDatetime = modifyDatetime;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Long getModifyUserId() {
        return modifyUserId;
    }

    public void setModifyUserId(Long modifyUserId) {
        this.modifyUserId = modifyUserId;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * 判断是否同一实例
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Entity entity = (Entity) obj;
        return id == null ? entity.id == null : id.equals(entity.id);
    }

    /**
     * @return 如果id属性为null，则返回super.hashCode()，否则返回id的hashCode
     */
    @Override
    public int hashCode() {
        return id == null ? super.hashCode() : id.hashCode();
    }
}