package com.hycjack.mybatis.annotationMapper;


import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import com.hycjack.mybatis.mapper.BaseMapperTest;
import com.hycjack.mybatis.model.SysRole;

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

}
