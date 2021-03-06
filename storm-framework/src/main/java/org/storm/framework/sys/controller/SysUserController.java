package org.storm.framework.sys.controller;

import com.github.pagehelper.Page;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.annotation.IsSearchCondition;
import org.storm.framework.base.controller.BaseController;
import org.storm.framework.base.exception.BusinessException;
import org.storm.framework.base.model.EntityUtil;
import org.storm.framework.base.util.SecurityManagerUtils;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.base.util.datatables.DatatablesView;
import org.storm.framework.base.util.datatables.SearchCondition;
import org.storm.framework.base.util.encryption.XXTea;
import org.storm.framework.base.util.web.EntityUtils;
import org.storm.framework.base.util.web.JsonMenuUtils;
import org.storm.framework.base.util.web.RequestUtils;
import org.storm.framework.base.util.web.ResponseUtils;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysRole;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.model.SysUserLogin;
import org.storm.framework.sys.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/sys/user")
public class SysUserController extends BaseController<SysUser, SysUserService> {

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRefUserRoleService sysRefUserRoleService;
    @Autowired
    private SysRefRoleMenuService sysRefRoleMenuService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private SysUserLoginService sysUserLoginService;

    @Override
    protected String getPrefix() {
        return VIEW + "/sysUser/sysUser";
    }

    @Override
    protected SysUserService getBaseService() {
        return sysUserService;
    }

    @Override
    protected SysUser getBaseEntity() {
        return new SysUser();
    }

    /**
     * 登录
     *
     * @param user
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/login.action")
    public String login(SysUser user, HttpServletRequest request, Model model) {
        String code = user.getCode();
        String pwd = user.getPwd();
        if (code != null && pwd != null) {
            try {
                user = (SysUser) SecurityManagerUtils.login(code, pwd);
                SecurityManagerUtils.setSessionAttribute(SysConstants.SYS_LOGIN_KEY, user);
            } catch (IncorrectCredentialsException ex) {
                user = null;
                model.addAttribute("error", "用户名或密码不正确！");
                logger.debug("debug at login: IncorrectCredentialsException");
            } catch (DisabledAccountException ex) {
                user = null;
                model.addAttribute("error", "帐号被禁用,请联系管理员！");
                logger.debug("debug at login: DisabledAccountException");
            } catch (UnknownAccountException ex) {
                user = null;
                model.addAttribute("error", "帐号不存在！");
                logger.debug("debug at login: UnknownAccountException");
            } catch (ExcessiveAttemptsException ex) {
                user = null;
                model.addAttribute("error", "登录失败次数过多！");
                logger.debug("debug at login: ExcessiveAttemptsException");
            } catch (ExpiredCredentialsException ex) {
                user = null;
                model.addAttribute("error", "帐号登录过期！");
                logger.debug("debug at login: ExpiredCredentialsException");
            }
            if (user != null) {
                List<Long> roleIds = sysRefUserRoleService.getRolesIdByUserId(user.getId());
                JSONArray menuArray = new JSONArray();
                Map<String, SysMenu> operateMap = new HashMap<>();
                try {
                    if (roleIds.size() > 0) {
                        List<SysMenu> sysMenuList = sysMenuService.getListByRoleIds(roleIds);
                        for (SysMenu menu : sysMenuList) {
                            if (menu.getIsActive() == SysConstants.EStatus.Valid.ordinal()) {
                                // 添加到菜单列表
                                if (menu.getType() == SysConstants.EResourceType.Menu.ordinal()) {
                                    menuArray.add(menu);
                                } else if (menu.getType() == SysConstants.EResourceType.Button.ordinal()) {
                                    // 按钮为增删改操作
                                    operateMap.put(menu.getUrl(), menu);
                                }
                            }
                        }
                    }
                    long t1 = System.currentTimeMillis();
                    JSONArray menuTree = JsonMenuUtils.treeMenuList(menuArray, 0, "submenu");
                    long t2 = System.currentTimeMillis();
                    logger.debug("组装菜单花费时间：" + (t2 - t1) + "ms");
                    SecurityManagerUtils.setSessionAttribute(SysConstants.SYS_USER_MENU, menuTree);
                    SecurityManagerUtils.setSessionAttribute(SysConstants.SYS_OPERATE_KEY, operateMap);
                    // 登录记录
                    SysUserLogin sysUserLogin = new SysUserLogin();
                    sysUserLogin.setUserId(user.getId());
                    sysUserLogin.setLoginDatetime(new Date());
                    sysUserLogin.setIp(RequestUtils.getIpAddr(request));
                    sysUserLogin.setUserAgent(request.getHeader("User-Agent"));
                    sysUserLoginService.save(sysUserLogin);
                } catch (Exception ex) {
                    logger.error("error at login: " + ExceptionUtils.getFullStackTrace(ex));
                    return "login";
                }
            }
            if (user == null) {
                return "login";
            } else {
                return "redirect:/index.action";
            }
        } else {
            return "login";
        }
    }

    /**
     * 注销，退出系统
     *
     * @return
     */
    @RequestMapping("/logout.action")
    public String logout(HttpServletRequest request) {
        SecurityManagerUtils.logout();
        return "redirect:/index.action";
    }

    @Override
    public ModelAndView edit(Long id, HttpServletRequest request, Model model) {
        String viewType = request.getParameter("viewType");
        if (id != null && id > 0) {
            SysUser te = this.getBaseService().getById(id);
            String pwd = XXTea.decrypt(te.getPwd(), "UTF-8", EntityUtil.bytesToHexString(EntityUtil.encryptSecretKey.getBytes()));
            te.setPwd(pwd);
            model.addAttribute(MODEL_NAME, te);
            logger.warn("entity:" + JSONObject.fromObject(te));
        }
        model.addAttribute("viewType", viewType);
        return new ModelAndView(EDIT);
    }

    @Override
    public String save(HttpServletRequest request) throws BusinessException {
        Map<String, Object> params = RequestUtils.getParameterByEdit(request);
        SysUser entity = getBaseEntity();
        logger.info("params:" + params.toString());
        EntityUtils.populate(entity, params);

        boolean isTrue = true;
        String msg = "保存成功！";
        String pwd = "";
        try {
            pwd = XXTea.encrypt(entity.getPwd(), "UTF-8", EntityUtil.bytesToHexString(EntityUtil.encryptSecretKey.getBytes()));
        } catch (Exception e) {
        }

        SysUser existsUser = sysUserService.getUserByCode(entity.getCode());

        logger.info("entity:" + JSONObject.fromObject(entity));
        Date nowDate = new Date();
        SysUser user = (SysUser) SecurityManagerUtils.getSessionAttribute(SysConstants.SYS_LOGIN_KEY);
        do {
            if (entity.getId() != null && entity.getId() > 0) {
                SysUser existEntity = this.getBaseService().getById(entity.getId());
                EntityUtils.populate(existEntity, params);
                existEntity.setModifyDatetime(nowDate);
                if (user != null) {
                    existEntity.setModifyUserId(user.getId());
                }

                if (existsUser != null && !existEntity.getId().equals(existsUser.getId())) {
                    isTrue = false;
                    msg = "该账号已存在！";
                    break;
                }

                existEntity.setPwd(pwd);

                this.getBaseService().update(existEntity);
            } else {
                if (existsUser != null) {
                    isTrue = false;
                    msg = "该账号已存在！";
                    break;
                }

                entity.setPwd(pwd);

                if (user != null) {
                    entity.setCreateUser(user.getId());
                    entity.setCreateUserId(user.getId());
                    entity.setModifyUserId(user.getId());
                }
                entity.setCreateDatetime(nowDate);
                entity.setModifyDatetime(nowDate);
                this.getBaseService().save(entity);
            }
        } while (false);

        return ResponseUtils.responseJsonResult(isTrue, msg);
    }

    @Override
    public String delete(HttpServletRequest request) {
        String ids = request.getParameter("ids");
        if (StringUtils.isNotBlank(ids)) {
            if (ids.indexOf(",") >= 0) {
                this.getBaseService().deleteByIds(ids);
            } else {
                this.getBaseService().deleteById(Long.parseLong(ids));
            }

        }
        return ResponseUtils.responseJsonResult(true);
    }

    @RequestMapping("/editUserRoles.action")
    public ModelAndView editUserRoles(HttpServletRequest request, Model model) {
        long userId = RequestUtils.getLong(request, "userId", 0);
        model.addAttribute("userId", userId);
        return new ModelAndView(VIEW + "/sysUser/setRoles");
    }

    @RequestMapping("/getUserRolesJson.action")
    @ResponseBody
    public String getUserRolesJson(HttpServletRequest request, @IsSearchCondition SearchCondition condition) {
        Map<String, Object> paramMap = RequestUtils.getParameterMap(request);
        logger.info("params:" + paramMap.toString());

        long userId = RequestUtils.getLong(request, "userId", 0);

        int draw = condition.getDraw();
        int pageNo = condition.getStart();
        int pageSize = condition.getLength();
        int columnIndex = condition.getOrders().get(0).getColumn();
        String sort = condition.getColumns().get(columnIndex).getData();
        String order = condition.getOrders().get(0).getDir();

        if (StringUtils.isNotBlank(sort)) {
            paramMap.put("sort", sort);
            paramMap.put("order", order);
        }

        long filtered = sysRoleService.getCount(paramMap);
        List<SysRole> roleList = sysRoleService.getPageList(paramMap, pageNo, pageSize);
        Page page = (Page) roleList;
        long total = page.getTotal();

        List<SysRole> list = new ArrayList<>();
        if (userId > 0) {
            Set<Long> roleSet = new HashSet<>();
            List<SysRole> userRoleList = sysRoleService.getUserRoles(userId);
            if (userRoleList != null && userRoleList.size() > 0) {
                for (SysRole sysRole : userRoleList) {
                    roleSet.add(sysRole.getId());
                }
            }
            if (roleList != null && roleList.size() > 0) {
                for (SysRole sysRole : roleList) {
                    if (sysRole.getStatus() == SysConstants.EStatus.InValid.ordinal()) {
                        continue;
                    }
                    if (roleSet.contains(sysRole.getId())) {
                        sysRole.setStatus((byte) 0);
                    } else {
                        sysRole.setStatus((byte) 1);
                    }
                    list.add(sysRole);
                }
            }
        }

        DatatablesView<SysRole> view = new DatatablesView<>();
        view.setDraw(draw);
        view.setRecordsTotal(total);
        view.setRecordsFiltered(filtered);
        view.setData(list);

        return JSONObject.fromObject(view).toString();
    }

    @RequestMapping("/setRoles.action")
    @ResponseBody
    public String setRoles(HttpServletRequest request) {
        long userId = RequestUtils.getLong(request, "userId", 0);
        String roleIds = request.getParameter("roleIds");
        sysUserService.setRolesForUser(userId, roleIds);
        return ResponseUtils.responseJsonResult(true);
    }
}
