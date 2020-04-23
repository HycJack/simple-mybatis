package com.hycjack.mybatis.pagehelper;


import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 自定义分页插件
 *
 * @author hycjack
 */
@Intercepts({
       /* @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class
        }),
        @Signature(type = Executor.class, method = "query", args = {
                MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class,
                CacheKey.class, BoundSql.class
        }),*/
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class}),
})
public class PageInterceptor implements Interceptor {

    /**
     * 默认页码
     */
    private Integer defaultPageIndex;
    /**
     * 默认每页数据条数
     */
    private Integer defaultPageSize;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = getUnProxyObject(invocation);
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        String sql = getSql(metaObject);
        if (!checkSelect(sql)) {
            // 不是select语句，进入责任链下一层
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        Page page = getPage(parameterObject);
        if (page == null) {
            // 没有传入page对象，不执行分页处理，进入责任链下一层
            return invocation.proceed();
        }

        // 设置分页默认值
        if (page.getPageNum() == null) {
            page.setPageNum(this.defaultPageIndex);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(this.defaultPageSize);
        }
        // 设置分页总数，数据总数
        setTotalToPage(page, invocation, metaObject, boundSql);
        // 校验分页参数
        checkPage(page);
        return changeSql(invocation, metaObject, boundSql, page);
    }

    @Override
    public Object plugin(Object target) {
        // 生成代理对象
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 初始化配置的默认页码，无配置则默认1
        this.defaultPageIndex = Integer.parseInt(properties.getProperty("defaultPageIndex", "1"));
        // 初始化配置的默认数据条数，无配置则默认20
        this.defaultPageSize = Integer.parseInt(properties.getProperty("defaultPageSize", "20"));
    }

    /**
     * 从代理对象中分离出真实对象
     *
     * @param invocation
     * @return
     */
    private StatementHandler getUnProxyObject(Invocation invocation) {
        // 取出被拦截的对象
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaStmtHandler = SystemMetaObject.forObject(statementHandler);
        Object object = null;
        // 分离代理对象
        while (metaStmtHandler.hasGetter("h")) {
            object = metaStmtHandler.getValue("h");
            metaStmtHandler = SystemMetaObject.forObject(object);
        }

        return object == null ? statementHandler : (StatementHandler) object;
    }

    /**
     * 判断是否是select语句
     *
     * @param sql
     * @return
     */
    private boolean checkSelect(String sql) {
        // 去除sql的前后空格，并将sql转换成小写
        sql = sql.trim().toLowerCase();
        return sql.indexOf("select") == 0;
    }

    /**
     * 获取分页参数
     *
     * @param parameterObject
     * @return
     */
    private Page getPage(Object parameterObject) {
        if (parameterObject == null) {
            return null;
        }

        if (parameterObject instanceof Map) {
            // 如果传入的参数是map类型的，则遍历map取出Page对象
            Map<String, Object> parameMap = (Map<String, Object>) parameterObject;
            Set<String> keySet = parameMap.keySet();
            for (String key : keySet) {
                Object value = parameMap.get(key);
                if (value instanceof Page) {
                    // 返回Page对象
                    return (Page) value;
                }
            }
        } else if (parameterObject instanceof Page) {
            // 如果传入的是Page类型，则直接返回该对象
            return (Page) parameterObject;
        }

        // 初步判断并没有传入Page类型的参数，返回null
        return null;
    }

    /**
     * 获取数据总数
     *
     * @param invocation
     * @param metaObject
     * @param boundSql
     * @return
     */
    private Long getTotal(Invocation invocation, MetaObject metaObject, BoundSql boundSql) {
        // 获取当前的mappedStatement对象
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 获取配置对象
        Configuration configuration = mappedStatement.getConfiguration();
        // 获取当前需要执行的sql
        String sql = getSql(metaObject);
        // 改写sql语句，实现返回数据总数 $_paging取名是为了防止数据库表重名
        String countSql = "select count(*) as total from (" + sql + ") $_paging";
        // 获取拦截方法参数，拦截的是connection对象
        Connection connection = (Connection) invocation.getArgs()[0];
        PreparedStatement pstmt = null;
        Long total = 0L;

        try {
            // 预编译查询数据总数的sql语句
            pstmt = connection.prepareStatement(countSql);
            // 构建boundSql对象
            BoundSql countBoundSql = new BoundSql(configuration, countSql, boundSql.getParameterMappings(),
                    boundSql.getParameterObject());
            // 构建parameterHandler用于设置sql参数
            ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(),
                    countBoundSql);
            // 设置sql参数
            parameterHandler.setParameters(pstmt);
            //执行查询
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                total = rs.getLong("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // 返回总数据数
        return total;
    }

    /**
     * 设置总数据数、总页数
     *
     * @param page
     * @param invocation
     * @param metaObject
     * @param boundSql
     */
    private void setTotalToPage(Page page, Invocation invocation, MetaObject metaObject, BoundSql boundSql) {
        // 总数据数
        long total = getTotal(invocation, metaObject, boundSql);
        // 计算总页数
        Integer totalPage = (int) (total/page.getPageSize());
        if (total % page.getPageSize() != 0) {
            totalPage = totalPage + 1;
        }

        page.setTotal(total);
        page.setPages(totalPage);
    }

    /**
     * 校验分页参数
     *
     * @param page
     */
    private void checkPage(Page page) {
        // 如果当前页码大于总页数，抛出异常
        if (page.getPageNum() > page.getPages()) {
            throw new RuntimeException("当前页码［" + page.getPageNum() + "］大于总页数［" + page.getPages() + "］");
        }
        // 如果当前页码小于总页数，抛出异常
        if (page.getPageNum() < 1) {
            throw new RuntimeException("当前页码［" + page.getPageNum() + "］小于［1］");
        }
    }

    /**
     * 修改当前查询的sql
     *
     * @param invocation
     * @param metaObject
     * @param boundSql
     * @param page
     * @return
     */
    private Object changeSql(Invocation invocation, MetaObject metaObject, BoundSql boundSql, Page page) throws Exception {
        // 获取当前查询的sql
        String sql = getSql(metaObject);
        // 修改sql，$_paging_table_limit取名是为了防止数据库表重名
        String newSql = "select * from (" + sql + ") $_paging_table_limit limit ?, ?";
        // 设置当前sql为修改后的sql
        setSql(metaObject, newSql);

        // 获取PreparedStatement对象
        PreparedStatement pstmt = (PreparedStatement) invocation.proceed();
        // 获取sql的总参数个数
        int parameCount = pstmt.getParameterMetaData().getParameterCount();
        // 设置分页参数
        pstmt.setInt(parameCount - 1, (page.getPageNum() - 1) * page.getPageSize());
        pstmt.setInt(parameCount, page.getPageSize());

        return pstmt;
    }

    /**
     * 获取当前查询的sql
     *
     * @param metaObject
     * @return
     */
    private String getSql(MetaObject metaObject) {
        return (String) metaObject.getValue("delegate.boundSql.sql");
    }

    /**
     * 设置当前查询的sql
     *
     * @param metaObject
     */
    private void setSql(MetaObject metaObject, String sql) {
        metaObject.setValue("delegate.boundSql.sql", sql);
    }

}
