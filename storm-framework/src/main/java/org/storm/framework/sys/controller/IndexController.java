package org.storm.framework.sys.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.storm.framework.base.util.SecurityManagerUtils;
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
        SysUser user = (SysUser) SecurityManagerUtils.getSessionAttribute(SysConstants.SYS_LOGIN_KEY);
        if (user != null && SecurityManagerUtils.getSessionAttribute(SysConstants.SYS_USER_MENU) != null) {
            model.addAttribute(SysConstants.SYS_USER_MENU, SecurityManagerUtils.getSessionAttribute(SysConstants.SYS_USER_MENU));
            model.addAttribute("admin", user);
            return new ModelAndView("index");
        } else {
            return new ModelAndView("login");
        }
    }

}
