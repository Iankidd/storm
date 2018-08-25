package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysServiceErrorLogMapper;
import org.storm.framework.sys.model.SysServiceErrorLog;
import org.storm.framework.sys.service.SysServiceErrorLogService;

@Service("sysServiceErrorLogService")
public class SysServiceErrorLogServiceImpl extends BaseServiceImpl<SysServiceErrorLog> implements SysServiceErrorLogService {

    @Autowired
    protected SysServiceErrorLogMapper sysServiceErrorLogMapper;

    @Override
    public BaseMapper<SysServiceErrorLog> getBaseMapper() {
        return sysServiceErrorLogMapper;
    }

    @Override
    public Class<SysServiceErrorLogMapper> getMpClass() {
        return SysServiceErrorLogMapper.class;
    }

}
