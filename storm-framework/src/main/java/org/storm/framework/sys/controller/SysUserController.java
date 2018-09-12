package org.storm.framework.sys.controller;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.controller.BaseController;
import org.storm.framework.base.exception.BusinessException;
import org.storm.framework.base.model.EntityUtil;
import org.storm.framework.base.util.*;
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
        return VIEW + "/sysUser/sysUser";
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
                JSONArray menuArray = new JSONArray();
                try {
                    if (roleIds.size() > 0) {
                        List<SysMenu> sysMenuList = sysMenuService.getListByRoleIds(roleIds);
                        for (SysMenu menu : sysMenuList) {
                            if (menu.getIsActive() == 0) {
                                // 添加到菜单列表
                                if (menu.getType() == SysConstants.EResourceType.Menu.ordinal()) {
                                    menuArray.add(menu);
                                }
                            }
                        }
                    }
                    long t1 = System.currentTimeMillis();
                    JSONArray menuTree = JsonMenuUtils.treeMenuList(menuArray, 0, "submenu");
                    long t2 = System.currentTimeMillis();
                    logger.info("组装菜单花费时间：" + (t2 - t1) + "ms");
                    session.setAttribute(SysConstants.SYS_USER_MENU, menuTree);
                    // 管理员在线记录
                    LoginUserUtils.addLoginUserMap(request.getServletContext(), user.getId(), session.getId().toString());
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
        Subject subject = SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();

        // 管理员下线记录
        LoginUserUtils.removeLoginUserMap(request.getServletContext(), user.getId());

        subject.logout();

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
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
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
}
