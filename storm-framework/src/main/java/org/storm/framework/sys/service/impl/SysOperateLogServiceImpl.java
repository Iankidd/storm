package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysOperateLogMapper;
import org.storm.framework.sys.model.SysOperateLog;
import org.storm.framework.sys.service.SysOperateLogService;

@Service("sysOperateLogService")
public class SysOperateLogServiceImpl extends BaseServiceImpl<SysOperateLog> implements SysOperateLogService {

    @Autowired
    protected SysOperateLogMapper sysOperateLogMapper;

    @Override
    public BaseMapper<SysOperateLog> getBaseMapper() {
        return sysOperateLogMapper;
    }

    @Override
    public Class<SysOperateLogMapper> getMpClass() {
        return SysOperateLogMapper.class;
    }

    @Async("asyncServiceExecutor")
    @Override
    public Long save(SysOperateLog paramT) {
        return super.save(paramT);
    }
}
