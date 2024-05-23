package cn.mybatis.mp.routing.datasource.test.dao;

import cn.mybatis.mp.core.mvc.Dao;
import cn.mybatis.mp.routing.datasource.test.DO.Hikari;

public interface DefaultTestDao extends Dao<Hikari, Integer> {

    void defaultDatabaseTest();
}
