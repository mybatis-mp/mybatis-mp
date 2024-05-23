package cn.mybatis.mp.routing.datasource.test.dao.impl;

import cn.mybatis.mp.core.mvc.impl.DaoImpl;
import cn.mybatis.mp.routing.datasource.DS;
import cn.mybatis.mp.routing.datasource.test.DO.Hikari;
import cn.mybatis.mp.routing.datasource.test.RoutingDataSourceType;
import cn.mybatis.mp.routing.datasource.test.dao.HikariDao;
import cn.mybatis.mp.routing.datasource.test.mapper.HikariMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DS(RoutingDataSourceType.HIKARI)
@Repository
public class HikariDaoImpl extends DaoImpl<Hikari, Integer> implements HikariDao {


    public HikariDaoImpl(@Autowired HikariMapper hikariMapper) {
        super(hikariMapper);
    }

    @Override
    public void test1() {
        this.getById(1);
    }

    @Override
    @DS(RoutingDataSourceType.HIKARI)
    public void test2() {
        this.getById(1);
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void test3() {
        test1();
    }

    private void aa() {
        this.getById(2);
    }

    public void aa3() {
        this.getById(2);
    }

    @Override
    public void test4() {
        this.aa();
    }
}
