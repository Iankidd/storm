package org.storm.service.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.storm.service.redis.service.RedisService;

public class MyShiroSessionListener implements SessionListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService redisService;

    @Override
    public void onStart(Session session) {
        logger.info("onStart: " + session.getId());
    }

    @Override
    public void onStop(Session session) {
        logger.info("onStop: " + session.getId());
        redisService.remove(session.getId().toString());
    }

    @Override
    public void onExpiration(Session session) {
        logger.info("onExpiration: " + session.getId());
        redisService.remove(session.getId().toString());
    }
}
