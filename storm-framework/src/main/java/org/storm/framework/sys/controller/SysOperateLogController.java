package org.storm.framework.sys.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.storm.framework.base.controller.BaseController;
import org.storm.framework.sys.model.SysOperateLog;
import org.storm.framework.sys.service.SysOperateLogService;

@Controller
@RequestMapping("/sys/sysOperateLog")
public class SysOperateLogController extends BaseController<SysOperateLog, SysOperateLogService> {
    @Autowired
    private SysOperateLogService sysOperateLogService;

    @Override
    protected String getPrefix() {
        return VIEW + "/sysOperateLog/sysOperateLog";
    }


    @Override
    protected SysOperateLogService getBaseService() {
        return sysOperateLogService;
    }

    @Override
    protected SysOperateLog getBaseEntity() {
        return new SysOperateLog();
    }

}
