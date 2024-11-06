package com.mybatis.mp.core.test.testCase.delete;

import cn.mybatis.mp.core.sql.executor.chain.DeleteChain;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import db.sql.api.DbType;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeleteTest extends BaseTest {

    @Test
    public void deleteAll() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            sysUserMapper.deleteAll();
            int cnt = sysUserMapper.count(where -> {
            });
            assertEquals(0, cnt);
        }
    }

    @Test
    public void onDBTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            int cnt = DeleteChain.of(sysUserMapper)
                    .dbAdapt((deleteChain, selector) -> {
                        selector.when(DbType.H2, () -> {
                                    deleteChain.eq(SysUser::getId, 3);
                                })
                                .when(DbType.MYSQL, () -> {
                                    deleteChain.eq(SysUser::getId, 2);
                                })
                                .otherwise(() -> {
                                    deleteChain.eq(SysUser::getId, 1);
                                });
                    })
                    .execute();
            assertEquals(cnt, 1);
            if (TestDataSource.DB_TYPE == DbType.H2) {
                assertNull(sysUserMapper.getById(3));
            } else if (TestDataSource.DB_TYPE == DbType.MYSQL) {
                assertNull(sysUserMapper.getById(2));
            } else {
                assertNull(sysUserMapper.getById(1));
            }
        }
    }

    @Test
    public void deleteIdTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = sysUserMapper.getById(1);
            System.out.println(sysUser);
            sysUserMapper.deleteById(1);
            sysUser = sysUserMapper.getById(1);
            assertNull(sysUser);
            List<SysUser> list = QueryChain.of(sysUserMapper).list();
            assertEquals(list.size(), 2);
        }
    }


    @Test
    public void deleteIdsTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            sysUserMapper.deleteByIds(1, 2);
            List<SysUser> list = QueryChain.of(sysUserMapper).list();
            assertEquals(list.size(), 1);
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            sysUserMapper.deleteByIds(Arrays.asList(1, 2));
            List<SysUser> list = QueryChain.of(sysUserMapper).list();
            assertEquals(list.size(), 1);
        }
    }

    @Test
    public void deleteEntityTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            sysUserMapper.delete(sysUserMapper.getById(1));
            List<SysUser> list = QueryChain.of(sysUserMapper).list();
            assertEquals(list.size(), 2);
        }
    }


    @Test
    public void deleteWithWhereTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            sysUserMapper.delete(where -> where.eq(SysUser::getId, 1));
            List<SysUser> list = QueryChain.of(sysUserMapper).list();
            assertEquals(list.size(), 2);
        }
    }
}
