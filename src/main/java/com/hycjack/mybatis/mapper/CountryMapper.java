package com.hycjack.mybatis.mapper;

import com.hycjack.mybatis.model.Country;

import java.util.List;

public interface CountryMapper {

    List<Country> selectAll();
}
