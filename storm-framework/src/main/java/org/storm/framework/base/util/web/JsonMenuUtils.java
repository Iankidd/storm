package org.storm.framework.base.util.web;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonMenuUtils {

    /**
     * 组装JSON菜单
     *
     * @param menuList
     * @param parentId
     * @param childTitle 子标识CODE
     * @return
     */
    public static JSONArray treeMenuList(JSONArray menuList, long parentId, String childTitle) {
        JSONArray childMenu = new JSONArray();
        for (Object object : menuList) {
            JSONObject jsonMenu = JSONObject.fromObject(object);
            long menuId = jsonMenu.getLong("id");
            long pid = jsonMenu.getLong("parentId");
            if (parentId == pid) {
                JSONArray c_node = treeMenuList(menuList, menuId, childTitle);
                jsonMenu.put(childTitle, c_node);
                childMenu.add(jsonMenu);
            }
        }
        return childMenu;
    }

    public static JSONArray treeMenuList(JSONArray menuList, String parentId, String childTitle) {
        JSONArray childMenu = new JSONArray();
        for (Object object : menuList) {
            JSONObject jsonMenu = JSONObject.fromObject(object);
            String menuId = jsonMenu.getString("id");
            String pid = jsonMenu.getString("parentId");
            if (parentId.equals(pid)) {
                JSONArray c_node = treeMenuList(menuList, menuId, childTitle);
                jsonMenu.put(childTitle, c_node);
                childMenu.add(jsonMenu);
            }
        }
        return childMenu;
    }

}
