package org.storm.service.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyShiroSessionListener implements SessionListener {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onStart(Session session) {
        logger.info("onStart: " + session.getId().toString());
    }

    @Override
    public void onStop(Session session) {
        logger.info("onStop: " + session.getId().toString());
    }

    @Override
    public void onExpiration(Session session) {
        logger.info("onExpiration: " + session.getId().toString());
    }
}
