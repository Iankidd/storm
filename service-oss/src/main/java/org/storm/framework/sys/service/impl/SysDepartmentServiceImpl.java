package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysDepartmentMapper;
import org.storm.framework.sys.model.SysDepartment;
import org.storm.framework.sys.service.SysDepartmentService;

@Service("sysDepartmentService")
public class SysDepartmentServiceImpl extends BaseServiceImpl<SysDepartment> implements SysDepartmentService {

    @Autowired
    protected SysDepartmentMapper sysDepartmentMapper;

    @Override
    public BaseMapper<SysDepartment> getBaseMapper() {
        return sysDepartmentMapper;
    }

    @Override
    public Class<SysDepartmentMapper> getMpClass() {
        return SysDepartmentMapper.class;
    }

}
