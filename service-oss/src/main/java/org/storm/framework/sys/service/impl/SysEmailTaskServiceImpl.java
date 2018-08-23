package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysEmailTaskMapper;
import org.storm.framework.sys.model.SysEmailTask;
import org.storm.framework.sys.service.SysEmailTaskService;

@Service("sysEmailTaskService")
public class SysEmailTaskServiceImpl extends BaseServiceImpl<SysEmailTask> implements SysEmailTaskService {

    @Autowired
    protected SysEmailTaskMapper sysEmailTaskMapper;

    @Override
    public BaseMapper<SysEmailTask> getBaseMapper() {
        return sysEmailTaskMapper;
    }

    @Override
    public Class<SysEmailTaskMapper> getMpClass() {
        return SysEmailTaskMapper.class;
    }

}
