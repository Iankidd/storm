package org.storm.service.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.storm.framework.sys.model.SysUser;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthorizeFilter extends AccessControlFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizeFilter.class);

    /**
     * 表示是否允许访问:
     * mappedValue就是[urls]配置中拦截器参数部分，如果允许访问返回true，否则false；
     * (感觉这里应该是对白名单（不需要登录的接口）放行的)
     * 如果isAccessAllowed返回true则onAccessDenied方法不会继续执行
     * 这里可以用来判断一些不被通过的链接（个人备注）
     *
     * @param servletRequest
     * @param servletResponse
     * @param o               表示写在拦截器中括号里面的字符串 mappedValue 就是 [urls] 配置中拦截器参数部分
     * @return
     * @throws Exception
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        boolean tag = true;
        Subject subject = getSubject(servletRequest, servletResponse);
        String url = getPathWithinApplication(servletRequest);

        SecurityUtils.getSubject();
        SysUser user = (SysUser) subject.getPrincipal();
        if (user == null) {
            logger.info("AuthorizeFilter：跳转到login页面！");
        } else {
            tag = false;
        }

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (tag) {
            if ((request.getHeader("accept") != null && request.getHeader("accept").indexOf("application/json") > -1)
                    || (request.getHeader("X-Requested-With") != null
                    && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") > -1)) {
                PrintWriter writer = null;
                response.setStatus(200);
                response.setContentType("application/json;charset=utf-8");
                try {
                    writer = response.getWriter();
                    writer.write("loginOut");
                    writer.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } finally {
                    if (writer != null)
                        writer.close();
                }
            } else {
                WebUtils.issueRedirect(servletRequest, servletResponse, "/index.action");
            }
        }
        logger.info("当前用户正在访问的 url => " + url + " [权限情况] => " + subject.isPermitted(url));
        return tag;
    }

    /**
     * 表示当访问拒绝时是否已经处理了；
     * 如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     * onAccessDenied是否执行取决于isAccessAllowed的值，
     * 如果返回true则onAccessDenied不会执行；如果返回false，执行onAccessDenied
     * 如果onAccessDenied也返回false，则直接返回，不会进入请求的方法
     * （只有isAccessAllowed和onAccessDenied的情况下）
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        return true;
    }
}
