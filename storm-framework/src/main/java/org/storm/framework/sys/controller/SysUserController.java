package org.storm.framework.sys.controller;

import net.sf.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.controller.BaseController;
import org.storm.framework.base.util.JsonMenuUtils;
import org.storm.framework.base.util.RequestUtils;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.model.SysUserLogin;
import org.storm.framework.sys.service.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        return "/sysUser/sysUser";
    }

    @Override
    protected SysUserService getBaseService() {
        return sysUserService;
    }

    @Override
    protected SysUser getBaseEntity() {
        SysUser sysUser = new SysUser();
        return sysUser;
    }

    /**
     * 登录
     *
     * @param user
     * @param request
     * @param model
     * @param httpSession
     * @return
     */
    @RequestMapping("/login.action")
    public ModelAndView login(SysUser user, HttpServletRequest request, Model model, HttpSession httpSession) {
        String code = user.getCode();
        String pwd = user.getPwd();
        if (code != null && pwd != null) {
            try {
                user = this.sysUserService.checkLogin(code, pwd);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (user != null) {
                List<Long> roleIds = sysRefUserRoleService.getRolesIdByUserId(user.getId());
                Map<String, Object> paramMap = null;
                Set<String> permissionSet = new HashSet<>();
                Map<String, SysMenu> operateMap = new HashMap<>();
                JSONArray menuArray = new JSONArray();
                try {
                    if (roleIds.size() > 0) {
                        List<Long> refRoleResourceIdsList = sysRefRoleMenuService.getIdsByRoleIds(roleIds);
                        if (refRoleResourceIdsList.size() > 0) {
                            paramMap = new HashMap<>();
                            paramMap.put("ids", refRoleResourceIdsList);
                            List<SysMenu> sysMenuList = sysMenuService.getList(paramMap);
                            for (SysMenu menu : sysMenuList) {
                                if (menu.getIsActive() == 0) {
                                    // 添加到权限对应的URL列表，用于在拦截器中的验证
                                    String url = menu.getUrl();
                                    if (StringUtils.isNotBlank(url) && !permissionSet.contains(url)) {
                                        permissionSet.add(url);
                                    }
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

                    }
                    long t1 = System.currentTimeMillis();
                    JSONArray menuTree = JsonMenuUtils.treeMenuList(menuArray, 0, "submenu");
                    long t2 = System.currentTimeMillis();
                    logger.info("组装菜单花费时间：" + (t2 - t1) + "ms");
                    httpSession.setAttribute(SysConstants.SYS_LOGIN_KEY, user);
                    httpSession.setAttribute(SysConstants.SYS_PERMISSION_KEY, permissionSet);
                    httpSession.setAttribute(SysConstants.SYS_USER_MENU, menuTree);
                    httpSession.setAttribute(SysConstants.SYS_OPERATE_KEY, operateMap);
                    model.addAttribute(SysConstants.SYS_USER_MENU, menuTree);
                    Map<Long, String> loginUserMap = (Map<Long, String>) request.getServletContext()
                            .getAttribute(SysConstants.LOGIN_USER_MAP);
                    if (loginUserMap == null) {
                        loginUserMap = new HashMap<>();
                    }

                    loginUserMap.put(user.getId(), request.getSession().getId());
                    request.getServletContext().setAttribute(SysConstants.LOGIN_USER_MAP, loginUserMap);
                    // 登录记录
                    SysUserLogin sysUserLogin = new SysUserLogin();
                    sysUserLogin.setUserId(user.getId());
                    sysUserLogin.setLoginDatetime(new Date());
                    sysUserLogin.setIp(RequestUtils.getIpAddr(request));
                    sysUserLogin.setUserAgent(request.getHeader("User-Agent"));
                    sysUserLoginService.save(sysUserLogin);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return new ModelAndView("login");
                }

            } else {
                model.addAttribute("error", "用户名或密码不正确！");
            }
            if (user == null) {
                return new ModelAndView("login");
            } else {
                return new ModelAndView("home");
            }
        } else {
            return new ModelAndView("login");
        }
    }

    /**
     * 注销，退出系统
     *
     * @param httpSession
     * @return
     */
    @RequestMapping("/logout.action")
    public ModelAndView logout(HttpSession httpSession) {
        httpSession.removeAttribute(SysConstants.SYS_LOGIN_KEY);
        httpSession.removeAttribute(SysConstants.SYS_PERMISSION_KEY);
        httpSession.removeAttribute(SysConstants.SYS_USER_MENU);
        return new ModelAndView("login");
    }
}
