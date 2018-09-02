package org.storm.framework.sys.controller;

import net.sf.json.JSONArray;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.storm.framework.base.controller.BaseController;
import org.storm.framework.base.util.JsonMenuUtils;
import org.storm.framework.base.util.RequestUtils;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.model.SysUserLogin;
import org.storm.framework.sys.service.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @return
     */
    @RequestMapping("/login.action")
    public String login(SysUser user, HttpServletRequest request, Model model) {
        String code = user.getCode();
        String pwd = user.getPwd();
        if (code != null && pwd != null) {
            Subject subject = SecurityUtils.getSubject();
            Session session = subject.getSession();
            try {
                UsernamePasswordToken token = new UsernamePasswordToken(code, pwd);
                subject.login(token);
                user = (SysUser) subject.getPrincipal();
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
                Map<String, SysMenu> operateMap = new HashMap<>();
                JSONArray menuArray = new JSONArray();
                try {
                    if (roleIds.size() > 0) {
                        List<SysMenu> sysMenuList = sysMenuService.getListByRoleIds(roleIds);
                        for (SysMenu menu : sysMenuList) {
                            if (menu.getIsActive() == 0) {
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
                    logger.info("组装菜单花费时间：" + (t2 - t1) + "ms");
                    session.setAttribute(SysConstants.SYS_USER_MENU, menuTree);
                    session.setAttribute(SysConstants.SYS_OPERATE_KEY, operateMap);
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
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return "redirect:/index.action";
    }
}
