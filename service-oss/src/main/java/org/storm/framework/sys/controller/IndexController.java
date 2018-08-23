package org.storm.framework.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.sys.model.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * 首页
     *
     * @param request
     * @param modelMap
     * @return
     */
    @GetMapping("/index.action")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        SysUser user = (SysUser) request.getSession().getAttribute(SysConstants.SYS_LOGIN_KEY);
        if (user != null && request.getSession().getAttribute(SysConstants.SYS_USER_MENU) != null) {
            request.setAttribute(SysConstants.SYS_USER_MENU,
                    request.getSession().getAttribute(SysConstants.SYS_USER_MENU));
            return new ModelAndView("index");
        } else {
            return new ModelAndView("login");
        }

    }
}
