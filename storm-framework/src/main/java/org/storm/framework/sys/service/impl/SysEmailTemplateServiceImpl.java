package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysEmailTemplateMapper;
import org.storm.framework.sys.model.SysEmailTemplate;
import org.storm.framework.sys.service.SysEmailTemplateService;

@Service("sysEmailTemplateService")
public class SysEmailTemplateServiceImpl extends BaseServiceImpl<SysEmailTemplate> implements SysEmailTemplateService {

    @Autowired
    protected SysEmailTemplateMapper sysEmailTemplateMapper;

    @Override
    public BaseMapper<SysEmailTemplate> getBaseMapper() {
        return sysEmailTemplateMapper;
    }

    @Override
    public Class<SysEmailTemplateMapper> getMpClass() {
        return SysEmailTemplateMapper.class;
    }

}
