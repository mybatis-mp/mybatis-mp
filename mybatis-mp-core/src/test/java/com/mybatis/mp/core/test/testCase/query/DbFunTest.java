package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import db.sql.api.DbType;
import db.sql.api.cmd.GetterFields;
import db.sql.api.impl.cmd.Methods;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbFunTest extends BaseTest {

    @Test
    public void whereAndGetterTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .and(SysUser::getId, c -> c.concat("x1").eq("2x1"))
                    .returnType(Integer.class)
                    .get();

            assertEquals(id, 2);

            SysUser sysUser = sysUserMapper.get(where -> where.and(SysUser::getId, c -> c.concat("x1").eq("2x1")));
            assertEquals(sysUser.getId(), 2);
        }
    }


    @Test
    public void whereAndGetterTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)

                    .from(SysUser.class)
                    .and(GetterFields.of(SysUser::getId, SysUser::getUserName), c -> c[0].eq(1))
                    .orderBy(GetterFields.of(SysUser::getId), c -> c[0])
                    .groupBy(GetterFields.of(SysUser::getId), c -> c[0])
                    .havingAnd(GetterFields.of(SysUser::getId, SysUser::getUserName), c -> c[0].count().gt(0))
                    .returnType(Integer.class)
                    .count();

            assertEquals(id, 1);
        }
    }

    @Test
    public void whereAndGetterTest3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .from(SysUser.class)
                    .and(GetterFields.of(SysUser::getId, SysUser::getUserName), cs -> cs[0].eq(1))
                    .orderBy(GetterFields.of(SysUser::getId), cs -> cs[0])
                    .groupBy(GetterFields.of(SysUser::getId), cs -> cs[0])
                    .havingAnd(GetterFields.of(SysUser::getId, SysUser::getUserName), cs -> cs[0].count().gt(0))
                    .returnType(Integer.class)
                    .count();

            assertEquals(id, 1);
        }
    }

    @Test
    public void ifTest() {
        if (TestDataSource.DB_TYPE == DbType.DB2) {
            return;
        }
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, c -> Methods.if_(c.eq(1), 2, 3))
                    .from(SysUser.class)
                    .eq(SysUser::getId, 1)
                    .returnType(Integer.class)
                    .get();

            assertEquals(id, 2);
        }
    }

    @Test
    public void instrTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer index = QueryChain.of(sysUserMapper)
                    .select(SysUser::getUserName, c -> c.instr("m"))
                    .from(SysUser.class)
                    .eq(SysUser::getId, 1)
                    .returnType(Integer.class)
                    .get();

            assertEquals(index, 3);
        }
    }


}
