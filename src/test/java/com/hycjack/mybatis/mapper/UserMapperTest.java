package com.hycjack.mybatis.mapper;

import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;
import com.hycjack.mybatis.mapper.UserMapper;
import com.hycjack.mybatis.model.SysRole;
import com.hycjack.mybatis.model.SysUser;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class UserMapperTest extends BaseMapperTest {

    @Test
    public void testInsert() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());

            int result = userMapper.insert(user);

            Assert.assertEquals(1, result);
            Assert.assertNotNull(user.getId());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }
    @Test
    public void testInsert2() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());

            int result = userMapper.insert2(user);

            Assert.assertEquals(1, result);
            Assert.assertNotNull(user.getId());
            System.out.println(user.getId());
        } finally {
            sqlSession.commit();
            sqlSession.close();
        }
    }
    @Test
    public void testInsert3() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());

            int result = userMapper.insert3(user);

            Assert.assertEquals(1, result);
            Assert.assertNotNull(user.getId());
            System.out.println(user.getId());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpdateById() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectById(1036L);
            Assert.assertNotNull(user);
            Assert.assertEquals("test1", user.getUserName());

            user.setUserName("test_admin");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test admin info");
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());

            int result = userMapper.updateById(user);

            Assert.assertEquals(1, result);
            user = userMapper.selectById(1036L);
            Assert.assertNotNull(user);
            Assert.assertEquals("test_admin", user.getUserName());
            System.out.println(user.getUserInfo());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testDeleteById() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectById(1036L);
            Assert.assertNotNull(user);
            Assert.assertEquals("test1", user.getUserName());

            int result = userMapper.deleteById(user);
            Assert.assertEquals(1, result);
            Assert.assertNull(userMapper.selectById(1036L));

            result = userMapper.deleteById(1037L);
            Assert.assertEquals(1, result);
            Assert.assertNull(userMapper.selectById(1037L));
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSelectById() {
        // 初始化加载配置文件，生成 sqlSessionFactory， 获取 SqlSession
        SqlSession sqlSession = getSqlSession();
        try {
            // 如何找到 Mapper 接口？
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            // 方法和 SQL 如何对应起來？
            SysUser user = userMapper.selectById(1L);
            Assert.assertNotNull(user);
            Assert.assertEquals("admin", user.getUserName());
        } finally {
            sqlSession.close();
        }
        assertTrue(true);
    }


    @Test
    public void testSelectAll() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> list = userMapper.selectAll();
            Assert.assertNotNull(list);
            Assert.assertTrue(list.size() > 0);
        } finally {
            sqlSession.close();
        }
        assertTrue(true);
    }
    @Test
    public void selectRoleByUserId() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysRole> list = userMapper.selectRoleByUserId(1L);
            Assert.assertNotNull(list);
            Assert.assertTrue(list.size() > 0);
        } finally {
            sqlSession.close();
        }
        assertTrue(true);
    }

    @Test
    public void testSelectRolesByUserAndRoleEnabled() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysRole> list = userMapper.selectRolesByUserAndRoleEnabled(1L, 1);
            Assert.assertNotNull(list);
            Assert.assertTrue(list.size() > 0);

            SysUser user = userMapper.selectById(1L);
            SysRole role = new SysRole();
            role.setEnabled(1);
            list = userMapper.selectRolesByUserAndRoleEnabled2(user, role);
            Assert.assertNotNull(list);
            Assert.assertTrue(list.size() > 0);
        } finally {
            sqlSession.close();
        }
        assertTrue(true);
    }
}
