package org.storm.service.shiro.config;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.storm.framework.base.model.EntityUtil;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.base.util.encryption.XXTea;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysRole;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.service.SysMenuService;
import org.storm.framework.sys.service.SysRoleService;
import org.storm.framework.sys.service.SysUserService;

import java.util.List;
import java.util.Set;

@Component
public class AdminShiroRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(AdminShiroRealm.class);

    @Autowired
    @Lazy
    private SysUserService sysUserService;
    @Autowired
    @Lazy
    private SysRoleService sysRoleService;
    @Autowired
    @Lazy
    private SysMenuService sysMenuService;

    /**
     * 配置权限 注入权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        Session session = SecurityUtils.getSubject().getSession();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (principals.getPrimaryPrincipal() instanceof SysUser) {
            //获取 session 存储的角色和权限
            List<SysRole> roleList = (List<SysRole>) session.getAttribute(SysConstants.SYS_ROLE_KEY);
            Set<String> permissionSet = (Set<String>) session.getAttribute(SysConstants.SYS_PERMISSION_KEY);
            String roleMsg = "";
            SysUser user = (SysUser) principals.getPrimaryPrincipal();
            //获取数据库存储的角色和权限
            if (roleList == null || permissionSet == null) {
                logger.debug("get permission from DB");
                try {
                    roleList = sysRoleService.getUserRoles(user.getId());
                    List<SysMenu> sysMenuList = sysMenuService.getListByRoles(roleList);
                    permissionSet = sysMenuService.getPermissionSet(sysMenuList);
                    session.setAttribute(SysConstants.SYS_ROLE_KEY, roleList);
                    session.setAttribute(SysConstants.SYS_PERMISSION_KEY, permissionSet);
                } catch (Exception e) {
                    logger.error("error at doGetAuthorizationInfo: " + ExceptionUtils.getFullStackTrace(e));
                }
            } else {
                logger.debug("get permission from session");
            }

            //注入角色(查询所有的角色注入控制器）
            for (SysRole role : roleList) {
                authorizationInfo.addRole(role.getCode());
                roleMsg = roleMsg + role.getCode() + ",";
            }
            //注入角色所有权限（查询用户所有的权限注入控制器）
            authorizationInfo.addStringPermissions(permissionSet);

            logger.info("Admin[" + user.getCode() + "]授权角色：" + roleMsg + " 权限：" + permissionSet.toString());
        }
        return authorizationInfo;
    }

    /**
     * 用户验证
     *
     * @param authcToken 账户数据
     * @return
     * @throws AuthenticationException 根据账户数据查询账户。根据账户状态抛出对应的异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {

        //获取用户的输入的账号
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        //这里需注意。看别人的教程有人是这样写的String password = (String) token.getCredentials();
        //项目运行的时候报错，发现密码不正确。后来进源码查看发现将密码注入后。Shiro会进行转义将字符串转换成字符数组。
        //源码：this(username, password != null ? password.toCharArray() : null, false, null);
        //不晓得是否是因为版本的原因，建议使用的时候下载源码进行查看
        String password = String.valueOf(token.getPassword());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        //DisabledAccountException      (禁用的帐号)
        //LockedAccountException        (锁定的帐号)
        //UnknownAccountException       (错误的帐号)
        //ExcessiveAttemptsException    (登录失败次数过多)
        //IncorrectCredentialsException (错误的凭证)
        //ExpiredCredentialsException   (过期的凭证)
        SysUser user = sysUserService.getUserByCode(username);
        String pwd = "";
        if (null == user) {
            throw new UnknownAccountException();
        } else {
            try {
                pwd = XXTea.encrypt(password, "UTF-8", EntityUtil.bytesToHexString(EntityUtil.encryptSecretKey.getBytes()));
            } catch (Exception e) {
                logger.error("error at encrypt password!");
            }
            if (pwd.equals(user.getPwd())) {
                if (user.getStatus() == SysConstants.EStatus.InValid.ordinal()) {
                    throw new DisabledAccountException();
                } else {
                    return new SimpleAuthenticationInfo(user, password, getName());
                }
            } else {
                throw new IncorrectCredentialsException();
            }
        }
    }

    @Override
    protected void doClearCache(PrincipalCollection principals) {
        super.doClearCache(principals);
        clearCachedAuthenticationInfo(principals);
        clearCachedAuthorizationInfo(principals);
    }

    @Override
    protected void clearCachedAuthenticationInfo(PrincipalCollection principals) {
        super.clearCachedAuthenticationInfo(principals);
        Object key = principals.getPrimaryPrincipal();
        getAuthenticationCache().remove(key);
    }

    @Override
    protected void clearCachedAuthorizationInfo(PrincipalCollection principals) {
        super.clearCachedAuthorizationInfo(principals);
        Object key = getAuthorizationCacheKey(principals);
        getAuthorizationCache().remove(key);
    }

    @Override
    protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
        return principals.getPrimaryPrincipal();
    }

}
