package org.storm.framework.base.util.web;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;


public class ResponseUtils {
    private final static String SUCCESS_MSG = "操作成功！";
    private final static String ERROR_MSG = "服务器错误，请联系管理员！";

    public static String responseJsonResult(boolean success) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        JSONObject object = new JSONObject();
        if (success) {
            jsonMap.put("code", 200);
            jsonMap.put("msg", SUCCESS_MSG);
            object = JSONObject.fromObject(jsonMap);
        } else {
            jsonMap.put("code", 500);
            jsonMap.put("msg", ERROR_MSG);
            object = JSONObject.fromObject(jsonMap);
        }
        return object.toString();
    }

    public static String responseJsonResult(boolean success, String msg) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        JSONObject object = new JSONObject();
        if (success) {
            if (StringUtils.isBlank(msg)) {
                msg = SUCCESS_MSG;
            }
            jsonMap.put("code", 200);
            jsonMap.put("msg", msg);
            object = JSONObject.fromObject(jsonMap);

        } else {
            if (StringUtils.isBlank(msg)) {
                msg = ERROR_MSG;
            }
            jsonMap.put("code", 500);
            jsonMap.put("msg", msg);
            object = JSONObject.fromObject(jsonMap);
        }
        return object.toString();
    }

}