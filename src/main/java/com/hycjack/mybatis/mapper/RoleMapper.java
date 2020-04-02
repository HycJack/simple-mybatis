package com.hycjack.mybatis.mapper;

import com.hycjack.mybatis.model.SysRole;
import org.apache.ibatis.annotations.Select;

public interface RoleMapper {
    @Select({"select id, role_name, enabled, create_by createBy, create_time createTime ",
            "from sys_role",
            "where od = #{id}"})
    SysRole selectById(Long id);
}
