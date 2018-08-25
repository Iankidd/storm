package org.storm.framework.sys.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.storm.framework.base.mapper.BaseMapper;
import org.storm.framework.sys.model.SysRefUserRole;

import java.util.List;

@Component
public interface SysRefUserRoleMapper extends BaseMapper<SysRefUserRole> {

    List<Long> getRolesIdByUserId(@Param("userId") Long userId);

    void deleteUserRoleByUserId(@Param("userId") Long userId);

}
