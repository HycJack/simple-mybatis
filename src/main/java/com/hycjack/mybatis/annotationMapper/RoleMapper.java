package com.hycjack.mybatis.annotationMapper;

import com.hycjack.mybatis.model.SysRole;
import org.apache.ibatis.annotations.Select;

/**
 * @author hycjack
 */
public interface RoleMapper {

    @Select({"select id, role_name, enabled, create_by createBy, create_time createTime ",
             "from sys_role",
             "where id = #{id}"})
    SysRole selectById(Long id);

}
