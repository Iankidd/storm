package org.storm.framework.sys.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.controller.BaseController;
import org.storm.framework.base.exception.BusinessException;
import org.storm.framework.base.util.EntityUtils;
import org.storm.framework.base.util.RequestUtils;
import org.storm.framework.base.util.ResponseUtils;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.base.util.ztree.TreeNode;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysRefUserRole;
import org.storm.framework.sys.model.SysRole;
import org.storm.framework.sys.service.SysMenuService;
import org.storm.framework.sys.service.SysRefRoleMenuService;
import org.storm.framework.sys.service.SysRefUserRoleService;
import org.storm.framework.sys.service.SysRoleService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/sys/role")
public class SysRoleController extends BaseController<SysRole, SysRoleService> {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysRefRoleMenuService sysRefRoleMenuService;
    @Autowired
    private SysRefUserRoleService sysRefUserRoleService;

    @Override
    protected String getPrefix() {
        return VIEW + "/sysRole/sysRole";
    }

    @Override
    protected SysRoleService getBaseService() {
        return sysRoleService;
    }

    @Override
    protected SysRole getBaseEntity() {
        return new SysRole();
    }

    @RequestMapping("/save.action")
    @ResponseBody
    @Override
    public String save(HttpServletRequest request) throws BusinessException {
        Map<String, Object> params = RequestUtils.getParameterByEdit(request);
        entity = getBaseEntity();
        logger.info("params:" + params.toString());
        EntityUtils.populate(entity, params);
        logger.info("entity:" + JSONObject.fromObject(entity));
        Date nowDate = new Date();
        SysRole oldEntity = this.getBaseService().getRoleByCode(entity.getCode());
        if (entity.getId() != null && entity.getId() > 0) {
            SysRole existEntity = this.getBaseService().getById(entity.getId());
            if (oldEntity != null && !oldEntity.getId().equals(existEntity.getId())) {
                return ResponseUtils.responseJsonResult(false, "该CODE已存在,请重新输入");
            }
            EntityUtils.populate(existEntity, params);
            existEntity.setModifyDatetime(nowDate);
            this.getBaseService().update(existEntity);
        } else {
            if (oldEntity != null) {
                return ResponseUtils.responseJsonResult(false, "该CODE已存在,请重新输入");
            }
            entity.setCreateDatetime(nowDate);
            entity.setModifyDatetime(nowDate);
            this.getBaseService().save(entity);
        }

        return ResponseUtils.responseJsonResult(true);
    }

    @RequestMapping(value = "/auth.action")
    public ModelAndView auth(Long id, HttpServletRequest request, Model model) {
        String viewType = request.getParameter("viewType");
        boolean chkDisabled = false;
        if ("view".equals(viewType)) {
            chkDisabled = true;
        }
        Map<String, Object> arg = new HashMap<>();
        arg.put("isActive", SysConstants.EStatus.Valid.ordinal());
        List<SysMenu> list = sysMenuService.getList(arg);
        SysRole sysRole = sysRoleService.getById(id);
        Set<SysMenu> sysMenus = new HashSet<>();
        List<TreeNode> treeList = new ArrayList<>();
        if (sysRole != null) {
            List<Long> refRoleResourceIdsList = sysRefRoleMenuService.getIdsByRoleId(sysRole.getId());
            if (refRoleResourceIdsList != null && refRoleResourceIdsList.size() > 0) {
                Map<String, Object> paramMap = new HashMap<>();
                paramMap.put("ids", refRoleResourceIdsList);
                List<SysMenu> sysMenuList = sysMenuService.getList(paramMap);

                for (SysMenu sysMenu : sysMenuList) {
                    sysMenus.add(sysMenu);
                }
            }

            for (SysMenu sysMenu : list) {
                TreeNode tree = null;
                if (sysMenus.contains(sysMenu)) {
                    tree = new TreeNode(sysMenu.getId(), sysMenu.getParentId() == null ? 0 : sysMenu.getParentId(), sysMenu.getName(), true, true, chkDisabled);
                } else {
                    tree = new TreeNode(sysMenu.getId(), sysMenu.getParentId() == null ? 0 : sysMenu.getParentId(), sysMenu.getName(), false, false, chkDisabled);
                }
                treeList.add(tree);
            }

            JSONArray treeJson = JSONArray.fromObject(treeList);
            model.addAttribute("sysRole", sysRole);
            model.addAttribute("treeJson", treeJson);
        } else {
            model.addAttribute("errorMsg", "找不到该角色！");
        }

        return new ModelAndView(VIEW + "/sysRole/setMenu");
    }

    @RequestMapping("/authSave.action")
    @ResponseBody
    protected String authSave(HttpServletRequest request, Model model) throws BusinessException {
        boolean isTrue = true;
        String msg = null;
        String resources = request.getParameter("ids");
        List<Long> reIds = new ArrayList<>();
        logger.debug("resources:" + resources);
        long roleId = RequestUtils.getLong(request, "roleId", 1);
        try {
            if (StringUtils.isNotBlank(resources)) {
                StringTokenizer token = new StringTokenizer(resources, ",");
                while (token.hasMoreTokens()) {
                    String x = token.nextToken();
                    reIds.add(Long.parseLong(x));
                }
            }
            sysRoleService.saveMenusForRole(roleId, reIds);
        } catch (Exception ex) {
            isTrue = false;
            msg = "角色授权失败";
            logger.error("error at " + this.getClass().getName() + ".authSave: " + ExceptionUtils.getFullStackTrace(ex));
        }
        return ResponseUtils.responseJsonResult(isTrue, msg);
    }

    @RequestMapping("/delete.action")
    @ResponseBody
    @Override
    public String delete(HttpServletRequest request) {
        String id = request.getParameter("ids");
        if (StringUtils.isNotBlank(id)) {
            long roleId = Long.parseLong(id);
            List<Long> roleList = sysRefRoleMenuService.getIdsByRoleId(roleId);
            if (roleList != null && roleList.size() > 0) {
                return ResponseUtils.responseJsonResult(false, "该角色已设置菜单权限,不能删除!");
            }
            List<SysRefUserRole> userRoleList = sysRefUserRoleService.getListByRoleId(roleId);

            if (userRoleList != null && userRoleList.size() > 0) {
                return ResponseUtils.responseJsonResult(false, "已有用户绑定该角色,不能删除!");
            }
            this.getBaseService().deleteById(roleId);
        }
        return ResponseUtils.responseJsonResult(true);
    }
}
