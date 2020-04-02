package com.hycjack.mybatis.mapper;

import static org.junit.Assert.assertTrue;

import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import com.hycjack.mybatis.model.Country;

import java.util.List;

/**
 * Unit test for simple App.
 */
public class CountryMapperTest extends BaseMapperTest {


    /**
     * Rigorous Test :-)
     */
    @Test
    public void testSelectAll() {
        SqlSession sqlSession = getSqlSession();
        try {
            List<Country> countryList = sqlSession.selectList("com.hycjack.mybatis.mapper.CountryMapper.selectAll");
            printCountryList(countryList);
        } finally {
            sqlSession.close();
        }
        assertTrue(true);
    }

    private void printCountryList(List<Country> countryList) {
        for (Country country : countryList) {
            System.out.printf("%-4d%4s%4s\n", country.getId(), country.getCountryName(), country.getCountryCode());
        }
    }
}
