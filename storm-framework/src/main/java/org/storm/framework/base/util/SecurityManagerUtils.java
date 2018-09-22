package org.storm.framework.base.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;

public class SecurityManagerUtils {

    public static void setSessionAttribute(String key, Object obj) {
        SecurityUtils.getSubject().getSession().setAttribute(key, obj);
    }

    public static Object getSessionAttribute(String key) {
        return SecurityUtils.getSubject().getSession().getAttribute(key);
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static Object login(String code, String pwd) {
        UsernamePasswordToken token = new UsernamePasswordToken(code, pwd);
        SecurityUtils.getSubject().login(token);
        return SecurityUtils.getSubject().getPrincipal();
    }

    public static Object getUser() {
        return SecurityUtils.getSubject().getPrincipal();
    }
}
