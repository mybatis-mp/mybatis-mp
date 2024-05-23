package cn.mybatis.mp.routing.datasource.test.dao;

import cn.mybatis.mp.core.mvc.Dao;
import cn.mybatis.mp.routing.datasource.test.DO.Hikari;

public interface HikariDao extends Dao<Hikari, Integer> {
    void test1();

    void test2();

    void test3();

    void test4();

    void aa3();
}
