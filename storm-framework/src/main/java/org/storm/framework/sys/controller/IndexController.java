package org.storm.framework.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.util.SysConstants;
import org.storm.framework.sys.model.SysUser;

@Controller
@RequestMapping("/")
public class IndexController {

    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/index.action")
    public ModelAndView index(Model model) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        SysUser user = (SysUser) subject.getPrincipal();
        if (user != null && session.getAttribute(SysConstants.SYS_USER_MENU) != null) {
            model.addAttribute(SysConstants.SYS_USER_MENU, session.getAttribute(SysConstants.SYS_USER_MENU));
            return new ModelAndView("index");
        } else {
            return new ModelAndView("login");
        }
    }

}
