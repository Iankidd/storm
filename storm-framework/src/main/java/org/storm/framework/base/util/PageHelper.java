package org.storm.framework.base.util;

import com.github.pagehelper.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import java.util.List;

public class PageHelper extends com.github.pagehelper.PageHelper {

    /**
     * 封装成JSON分页参数
     *
     * @param list
     * @return
     */
    public static JSONObject toJson(List list) {
        JSONObject result = new JSONObject();
        if (list instanceof Page) {
            Page page = (Page) list;
            long total = page.getTotal();
            List rows = page;
            JSONArray rowsJSON = new JSONArray();
            rowsJSON = JSONArray.fromObject(rows);
            JsonConfig jc = DateJsonValueProcessor.configJson(DateJsonValueProcessor.exincludes, "yyyy-MM-dd HH:mm:ss");
            rowsJSON = JSONArray.fromObject(list, jc);
            result.put("total", total);
            result.put("rows", rowsJSON);
        }
        return result;
    }
}
