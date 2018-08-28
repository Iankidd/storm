package org.storm.service.shiro.config;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.sys.model.SysMenu;
import org.storm.framework.sys.model.SysRole;
import org.storm.framework.sys.model.SysUser;
import org.storm.framework.sys.service.SysMenuService;
import org.storm.framework.sys.service.SysRoleService;
import org.storm.framework.sys.service.SysUserService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class AdminShiroRealm extends AuthorizingRealm {

    private static Logger logger = LoggerFactory.getLogger(AdminShiroRealm.class);

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 配置权限 注入权限
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        logger.debug("开始Admin权限授权(进行权限验证!)");
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        if (principals.getPrimaryPrincipal() instanceof SysUser) {
            SysUser user = (SysUser) principals.getPrimaryPrincipal();
            logger.debug("当前Admin: " + user.getCode());
            Set<String> permissionSet = new HashSet<>();
            try {
                //注入角色(查询所有的角色注入控制器）
                String roleMsg = "";
                List<SysRole> list = sysRoleService.getUserRoles(user.getId());
                for (SysRole role : list) {
                    authorizationInfo.addRole(role.getCode());
                    roleMsg = role.getCode() + "," + roleMsg;
                }
                //注入角色所有权限（查询用户所有的权限注入控制器）
                List<SysMenu> sysMenuList = sysMenuService.getListByRoles(list);
                for (SysMenu menu : sysMenuList) {
                    if (menu.getIsActive() == 0) {
                        //添加到权限对应的URL列表，用于在拦截器中的验证
                        String url = menu.getUrl();
                        if (StringUtils.isNotBlank(url) && !permissionSet.contains(url)) {
                            permissionSet.add(url);
                        }
                    }
                }
                authorizationInfo.addStringPermissions(permissionSet);
                logger.info("当前Admin授权角色：" + roleMsg + " 权限：" + permissionSet.toString());
            } catch (Exception e) {
                logger.error("error at doGetAuthorizationInfo: " + ExceptionUtils.getFullStackTrace(e));
            }
        }
        return authorizationInfo;
    }

    /**
     * 用户验证
     *
     * @param token 账户数据
     * @return
     * @throws AuthenticationException 根据账户数据查询账户。根据账户状态抛出对应的异常
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        //获取用户的输入的账号
        String username = (String) token.getPrincipal();
        //这里需注意。看别人的教程有人是这样写的String password = (String) token.getCredentials();
        //项目运行的时候报错，发现密码不正确。后来进源码查看发现将密码注入后。Shiro会进行转义将字符串转换成字符数组。
        //源码：this(username, password != null ? password.toCharArray() : null, false, null);
        //不晓得是否是因为版本的原因，建议使用的时候下载源码进行查看
        String password = new String((char[]) token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        //DisabledAccountException      (禁用的帐号)
        //LockedAccountException        (锁定的帐号)
        //UnknownAccountException       (错误的帐号)
        //ExcessiveAttemptsException    (登录失败次数过多)
        //IncorrectCredentialsException (错误的凭证)
        //ExpiredCredentialsException   (过期的凭证)
        SysUser user = sysUserService.getUserByCode(username);
        if (null == user) {
            throw new UnknownAccountException();
        } else {
            if (password.equals(user.getPwd())) {
                if (user.getStatus() == SysConstants.EStatus.InValid.ordinal()) {
                    throw new DisabledAccountException();
                } else {
                    SimpleAuthenticationInfo authorizationInfo = new SimpleAuthenticationInfo(user, user.getPwd().toCharArray(), getName());
                    return authorizationInfo;
                }
            } else {
                throw new IncorrectCredentialsException();
            }
        }
    }
}
