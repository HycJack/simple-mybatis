package com.hycjack.mybatis.pagehelper.dialect;

public interface Dialect {

    boolean supportPage();

    String getPagingSql(String sql, int offset, int limit);
}
