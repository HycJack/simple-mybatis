package com.hycjack.mybatis.mapper;

import com.hycjack.mybatis.model.SysRole;
import com.hycjack.mybatis.model.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    SysUser selectById(Long id);
    int deleteById(Long id);
    int deleteById(SysUser sysUser);
    int updateById(SysUser sysUser);
    int insert(SysUser sysUser);
    int insert2(SysUser sysUser);
    int insert3(SysUser sysUser);
    List<SysUser> selectAll();
    List<SysRole> selectRoleByUserId(Long id);
    List<SysRole> selectRolesByUserAndRoleEnabled(@Param("userId") Long userId, @Param("enabled") Integer enabled);
    List<SysRole> selectRolesByUserAndRoleEnabled2(@Param("user") SysUser user, @Param("role") SysRole role);
}
