package com.hycjack.mybatis.pagehelper.dialect;

public class MySQLDialect implements Dialect {

    @Override
    public boolean supportPage() {
        return true;
    }

    @Override
    public String getPagingSql(String sql, int offset, int limit) {
        return null;
    }
}
