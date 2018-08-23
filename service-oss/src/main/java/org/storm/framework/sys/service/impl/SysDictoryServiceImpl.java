package org.storm.framework.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.base.service.impl.BaseServiceImpl;
import org.storm.framework.sys.mapper.SysDictoryMapper;
import org.storm.framework.sys.model.SysDictory;
import org.storm.framework.sys.service.SysDictoryService;

@Service("sysDictoryService")
public class SysDictoryServiceImpl extends BaseServiceImpl<SysDictory> implements SysDictoryService {

    @Autowired
    protected SysDictoryMapper sysDictoryMapper;

    @Override
    public BaseMapper<SysDictory> getBaseMapper() {
        return sysDictoryMapper;
    }

    @Override
    public Class<SysDictoryMapper> getMpClass() {
        return SysDictoryMapper.class;
    }

}
