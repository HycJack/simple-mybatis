package com.hycjack.mybatis.annotationMapper;


import com.hycjack.mybatis.pagehelper.Page;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import com.hycjack.mybatis.mapper.BaseMapperTest;
import com.hycjack.mybatis.model.SysRole;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class RoleMapperTest extends BaseMapperTest {
    @Test
    public void testSelectById1() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole role = roleMapper.selectById(1L);
            Assert.assertNotNull(role);
            Assert.assertEquals("管理员", role.getRoleName());
        } finally {
            sqlSession.close();
        }
        assertTrue(true);
    }

    @Test
    public void testListRoleByPage() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            Page page = new Page(1, 5);
            List<SysRole> roleList = roleMapper.listRoleByPage(page);
            System.out.println("===分页信息===");
            System.out.println("当前页码：" + page.getPageNum());
            System.out.println("每页显示数据数：" + page.getPageSize());
            System.out.println("总数据数：" + page.getTotal());
            System.out.println("总页数：" + page.getPages());
            System.out.println("=============");
            System.out.println("===数据列表===");
            for (SysRole r : roleList) {
                System.out.println(r);
            }
        } finally {
            sqlSession.close();
        }
        assertTrue(true);
    }

}
