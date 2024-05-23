package com.mybatis.mp.core.test.testCase.tenant;

import cn.mybatis.mp.core.sql.executor.chain.DeleteChain;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import cn.mybatis.mp.core.tenant.TenantContext;
import com.mybatis.mp.core.test.DO.TenantTest;
import com.mybatis.mp.core.test.mapper.TenantTestMapper;
import com.mybatis.mp.core.test.model.TenantModel;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TenantTestCase extends BaseTest {

    @BeforeEach
    public void before() {
        TenantContext.registerTenantGetter(() -> {
            return 1;
        });
    }

    @Test
    public void insertTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            TenantTestMapper tenantTestMapper = session.getMapper(TenantTestMapper.class);
            TenantTest tenantTest = new TenantTest();
            tenantTest.setName("我是1");
            tenantTest.setCreateTime(LocalDateTime.now());
            tenantTestMapper.save(tenantTest);
            System.out.println(tenantTest);
            assertNotNull(tenantTest.getId());
            assertEquals(1, (int) tenantTestMapper.getById(tenantTest.getId()).getTenantId());
        }
    }

    @Test
    public void updateTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            TenantTestMapper tenantTestMapper = session.getMapper(TenantTestMapper.class);
            TenantTest tenantTest = new TenantTest();
            tenantTest.setName("我是1");
            tenantTest.setCreateTime(LocalDateTime.now());
            tenantTestMapper.save(tenantTest);

            tenantTest.setName("我是2");
            tenantTestMapper.update(tenantTest);
            System.out.println(tenantTest);
            assertEquals(1, (int) tenantTest.getTenantId());


            TenantContext.registerTenantGetter(() -> {
                return 2;
            });
            tenantTest.setName("我是3");
            int updateCnt = tenantTestMapper.update(tenantTest);
            assertEquals(updateCnt, 0);
        }
    }


    @Test
    public void insertWithModelTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            TenantTestMapper tenantTestMapper = session.getMapper(TenantTestMapper.class);
            TenantModel tenantTest = new TenantModel();
            tenantTest.setName("我是1");
            tenantTest.setCreateTime(LocalDateTime.now());
            tenantTestMapper.save(tenantTest);
            System.out.println(tenantTest);
            assertNotNull(tenantTest.getId());
            assertEquals(1, (int) tenantTestMapper.getById(tenantTest.getId()).getTenantId());
        }
    }

    @Test
    public void updateWithModelTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            TenantTestMapper tenantTestMapper = session.getMapper(TenantTestMapper.class);
            TenantModel tenantTest = new TenantModel();
            tenantTest.setName("我是1");
            tenantTest.setCreateTime(LocalDateTime.now());
            tenantTestMapper.save(tenantTest);

            tenantTest.setName("我是2");
            tenantTestMapper.update(tenantTest);
            System.out.println(tenantTest);
            assertEquals(1, (int) tenantTest.getTenantId());


            TenantContext.registerTenantGetter(() -> {
                return 2;
            });
            tenantTest.setName("我是3");
            int updateCnt = tenantTestMapper.update(tenantTest);
            assertEquals(updateCnt, 0);
        }
    }


    @Test
    public void deleteTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            TenantTestMapper tenantTestMapper = session.getMapper(TenantTestMapper.class);
            TenantTest tenantTest = new TenantTest();
            tenantTest.setName("我是1");
            tenantTest.setCreateTime(LocalDateTime.now());
            tenantTestMapper.save(tenantTest);


            check("", "delete FROM tenant_test WHERE  (tenant_id = 1) AND  (id = '" + tenantTest.getId() + "')", DeleteChain.of(tenantTestMapper).delete(TenantTest.class).from(TenantTest.class).eq(TenantTest::getId, tenantTest.getId()));
            int count = QueryChain.of(tenantTestMapper).count();
            assertEquals(count, 1);

            TenantContext.registerTenantGetter(() -> {
                return 2;
            });

            count = QueryChain.of(tenantTestMapper).count();
            assertEquals(count, 0);

            check("", "delete FROM tenant_test WHERE  (tenant_id = 2) AND  (id = '" + tenantTest.getId() + "')", DeleteChain.of(tenantTestMapper).delete(TenantTest.class).from(TenantTest.class).eq(TenantTest::getId, tenantTest.getId()));

            TenantContext.registerTenantGetter(() -> {
                return 1;
            });

            count = QueryChain.of(tenantTestMapper).count();
            assertEquals(count, 1);

            tenantTestMapper.deleteById(tenantTest.getId());

            count = QueryChain.of(tenantTestMapper).count();
            assertEquals(count, 0);


        }
    }
}
