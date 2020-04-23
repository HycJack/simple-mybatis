package com.hycjack.mybatis.annotationMapper;

import com.hycjack.mybatis.model.SysRole;
import com.hycjack.mybatis.pagehelper.Page;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author hycjack
 */
public interface RoleMapper {

    @Select({"select id, role_name, enabled, create_by createBy, create_time createTime ",
             "from sys_role",
             "where id = #{id}"})
    SysRole selectById(Long id);

    @Select({"SELECT id roleId, role_name roleName,  create_by createBy, create_time createTime ",
            "from sys_role"})
    List<SysRole> listRoleByPage(Page page);

}
