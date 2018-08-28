package org.storm.framework.sys.mapper;

import org.springframework.stereotype.Component;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.sys.model.SysRole;

import java.util.List;

@Component
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> getUserRoles(Long userId);
}
