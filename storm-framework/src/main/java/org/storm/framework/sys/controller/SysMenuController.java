package org.storm.framework.sys.controller;

import com.github.pagehelper.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.annotation.IsSearchCondition;
import org.storm.framework.base.controller.BaseController;
import org.storm.framework.base.util.EntityUtils;
import org.storm.framework.base.util.JsonMenuUtils;
import org.storm.framework.base.util.RequestUtils;
import org.storm.framework.base.util.ResponseUtils;
import org.storm.framework.base.util.datatables.DatatablesView;
import org.storm.framework.base.util.datatables.SearchCondition;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.service.SysMenuService;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sys/menu")
public class SysMenuController extends BaseController<SysMenu, SysMenuService> {

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    protected String getPrefix() {
        return VIEW + "/sysMenu/sysMenu";
    }

    @Override
    protected SysMenuService getBaseService() {
        return sysMenuService;
    }

    @Override
    protected SysMenu getBaseEntity() {
        return new SysMenu();
    }

    @RequestMapping("/list.action")
    @Override
    public ModelAndView list(HttpServletRequest request) {
        long parentId = 0;
        request.setAttribute("parentId", parentId);
        return new ModelAndView(LIST);
    }

    @RequestMapping("/treeJson.action")
    @ResponseBody
    public String treeJson(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        String parentId = request.getParameter("parentId");
        List<SysMenu> list = this.getBaseService().getList(paramMap);
        JSONArray array = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("id", 0);
        object.put("text", "菜单");
        object.put("parentId", -1);
        array.add(object);
        for (SysMenu sysMenu : list) {
            object = new JSONObject();
            object.put("id", sysMenu.getId());
            object.put("text", sysMenu.getName());
            object.put("parentId", sysMenu.getParentId() == null ? 0 : sysMenu.getParentId());
            array.add(object);
        }
        logger.info("菜单[array]:" + array);
        JSONArray treeArray = null;
        if (StringUtils.isBlank(parentId)) {
            treeArray = JsonMenuUtils.treeMenuList(array, -1, "children");
        } else {
            treeArray = JsonMenuUtils.treeMenuList(array, Long.parseLong(parentId), "children");
        }
        logger.info("菜单[treeArray]:" + treeArray.toString());
        return treeArray.toString();
    }

    @Override
    public String pageJson(HttpServletRequest request, @IsSearchCondition SearchCondition condition) {
        Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
        logger.info("params:" + paramMap.toString());

        int draw = condition.getDraw();
        int pageNo = condition.getStart();
        int pageSize = condition.getLength();

        paramMap.put("sort", "sort");
        paramMap.put("order", "asc");

        long filtered = this.getBaseService().getCount(paramMap);
        List<SysMenu> list = this.getBaseService().getPageList(paramMap, pageNo, pageSize);
        Page page = (Page) list;
        long total = page.getTotal();

        DatatablesView<SysMenu> view = new DatatablesView<>();
        view.setDraw(draw);
        view.setRecordsTotal(total);
        view.setRecordsFiltered(filtered);
        view.setData(list);

        return JSONObject.fromObject(view).toString();
    }

    @RequestMapping(value = "/edit.action")
    @Override
    public ModelAndView edit(Long id, HttpServletRequest request, Model model) {
        String viewType = request.getParameter("viewType");
        long parentId = RequestUtils.getLong(request, "parentId", 0);
        String parentName = "菜单";
        if(parentId > 0) {
            SysMenu parentMenu = this.getBaseService().getById(parentId);
            if(parentMenu != null) {
                parentName = parentMenu.getName();
            }
        }
        if (id != null) {
            SysMenu te = this.getBaseService().getById(id);
            request.setAttribute(MODEL_NAME, te);
            logger.warn("entity:" + JSONObject.fromObject(te));
        }
        model.addAttribute("viewType", viewType);
        model.addAttribute("parentId", parentId);
        model.addAttribute("parent", parentName);
        return new ModelAndView(EDIT);
    }

    @RequestMapping("/save.action")
    @ResponseBody
    @Override
    public String save(HttpServletRequest request) {
        Map<String, Object> params = RequestUtils.getParameterByEdit(request);
        entity = getBaseEntity();
        logger.info("params:" + params.toString());
        EntityUtils.populate(entity, params);
        logger.info("entity:" + JSONObject.fromObject(entity));
        if (StringUtils.isBlank(entity.getUrl())) {
            entity.setIsLeaf((byte) 0);
        } else {
            entity.setIsLeaf((byte) 1);
        }
        Date nowDate = new Date();
        if (entity.getId() != null && entity.getId() > 0) {
            entity.setModifyDatetime(nowDate);
            this.getBaseService().update(entity);
        } else {
            entity.setCreateDatetime(nowDate);
            entity.setModifyDatetime(nowDate);
            this.getBaseService().save(entity);
        }
        return ResponseUtils.responseJsonResult(true);
    }
}
