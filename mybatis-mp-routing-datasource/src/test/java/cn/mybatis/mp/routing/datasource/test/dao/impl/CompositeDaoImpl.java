package cn.mybatis.mp.routing.datasource.test.dao.impl;

import cn.mybatis.mp.routing.datasource.DS;
import cn.mybatis.mp.routing.datasource.test.RoutingDataSourceType;
import cn.mybatis.mp.routing.datasource.test.dao.CompositeDao;
import cn.mybatis.mp.routing.datasource.test.dao.DruidDao;
import cn.mybatis.mp.routing.datasource.test.dao.HikariDao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Repository
@DS(RoutingDataSourceType.DRUID)
public class CompositeDaoImpl implements CompositeDao {

    @Resource
    private HikariDao hikariDao;

    @Resource
    private DruidDao druidDao;

    @Transactional
    @Override

    public void test4() {
        druidDao.test3();
        hikariDao.test3();


    }

    @Override
    @Transactional
    public void testSuperMethod() {
        druidDao.getById2(1);
        hikariDao.getById2(1);
    }
}
