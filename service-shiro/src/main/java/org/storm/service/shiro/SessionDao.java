package org.storm.service.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.Serializable;

public class SessionDao extends EnterpriseCacheSessionDAO {

    // 创建session，保存到redis
    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = super.doCreate(session);
        getCacheManager().getCache("shiro-activeSessionCache").put(session.getId().toString(), session);
        return sessionId;
    }

    // 获取session
    @Override
    protected Session doReadSession(Serializable sessionId) {
        return getActiveSessionsCache().get(sessionId);
    }

    // 更新session的最后一次访问时间
    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
        getActiveSessionsCache().put(session.getId().toString(), session);
    }

    // 删除session
    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
        getActiveSessionsCache().remove(session.getId().toString());
    }
}
