package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbFunTest extends BaseTest {

    @Test
    public void md5Test() {
//        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
//            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
//            Integer id = QueryChain.of(sysUserMapper)
//                    .select(SysUser::getId, c -> c.sin().as("x_sin"))
//                    .from(SysUser.class)
//                    .eq(SysUser::getId, 2)
//                    .and(queryChain -> {
//                        return queryChain.$(SysUser::getCreate_time, c -> c.date().eq("2023-12-10"));
//                    })
//                    .and(queryChain -> {
//                        return Methods.date(queryChain.$(SysUser::getCreate_time)).eq("2023-12-10");
//                    })
//                    .empty(SysUser::getUserName)
//                    .orderBy(SysUser::getId, c -> c.plus(1))
//                    .setReturnType(Integer.TYPE)
//                    .get();
//
//            Assert.assertEquals("eq", null, id);
//        }
    }


    @Test
    public void whereAndGetterTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .and(SysUser::getId, c -> c.concat("x1").eq("2x1"))
                    .returnType(Integer.TYPE)
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
                    .and(c -> c[0].eq(1), SysUser::getId, SysUser::getUserName)
                    .orderByWithFun(c -> c[0], SysUser::getId)
                    .groupByWithFun(c -> c[0], SysUser::getId)
                    .havingAnd(c -> c[0].count().gt(0), SysUser::getId, SysUser::getUserName)
                    .returnType(Integer.TYPE)
                    .count();

            assertEquals(id, 1);
        }
    }
}
