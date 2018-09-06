package org.storm.framework.base.util;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理员在线记录
 */
public class LoginUserUtils {

    private static Map<Long, String> getLoginUserMap(ServletContext context) {
        Map<Long, String> loginUserMap = (Map<Long, String>) context.getAttribute(SysConstants.LOGIN_USER_MAP);
        if (loginUserMap == null) {
            loginUserMap = new ConcurrentHashMap<>();
        }
        return loginUserMap;
    }

    private static int getLoginUserCount(ServletContext context) {
        Map<Long, String> loginUserMap = (Map<Long, String>) context.getAttribute(SysConstants.LOGIN_USER_MAP);
        if (loginUserMap == null) {
            return 0;
        }
        return loginUserMap.size();
    }

    public static void addLoginUserMap(ServletContext context, long userId, String seesionId) {
        getLoginUserMap(context).put(userId, seesionId);
    }

    public static void removeLoginUserMap(ServletContext context, long userId) {
        getLoginUserMap(context).remove(userId);
    }
}
