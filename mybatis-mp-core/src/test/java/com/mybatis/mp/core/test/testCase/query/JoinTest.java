package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.executor.SubQuery;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import db.sql.api.cmd.JoinMode;
import db.sql.api.impl.cmd.dbFun.FunctionInterface;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class JoinTest extends BaseTest {

    @Test
    public void defaultAddOn() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .returnType(Integer.TYPE)
                    .get();


            assertEquals(Integer.valueOf(2), count, "defaultAddOn");
        }
    }

    @Test
    public void customAddOn() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class, on -> on.eq(SysUser::getRole_id, SysRole::getId).like(SysUser::getUserName, "test1"))
                    .returnType(Integer.TYPE)
                    .get();


            assertEquals(Integer.valueOf(1), count, "customAddOn");
        }
    }

    @Test
    public void innerJoin() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(2), count, "innerJoin");
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Map<String, Object> map = QueryChain.of(sysUserMapper)
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .returnType(Map.class)
                    .orderBy(SysUser::getId)
                    .limit(1)
                    .get();
            assertNotNull(map);
            assertTrue(map instanceof Map);
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<Map<String, Object>> maps = QueryChain.of(sysUserMapper)
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .returnMap(Object.class)
                    .list();
            assertEquals(3, maps.size());
        }
    }

    @Test
    public void leftJoin() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(JoinMode.LEFT, SysUser.class, SysRole.class)
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(3), count, "leftJoin");
        }
    }

    @Test
    public void rightJoin() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, c -> c.count())
                    .from(SysUser.class)
                    .join(JoinMode.RIGHT, SysUser.class, SysRole.class)
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(2), count, "rightJoin");
        }
    }

    @Test
    public void fullJoin() {

        Query query = Query.create()
                .select(SysUser::getId, FunctionInterface::count)
                .from(SysUser.class)
                .join(JoinMode.FULL, SysUser.class, SysRole.class);

        query.setReturnType(Integer.TYPE);
        check("fullJoin", "SELECT  COUNT( t.id) FROM t_sys_user t  FULL OUTER JOIN sys_role t2 ON  t2.id =  t.role_id", query);

    }

    @Test
    public void joinSelf() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(JoinMode.INNER, SysUser.class, 1, SysUser.class, 2, on -> on.eq(SysUser::getId, 1, SysUser::getRole_id, 2))
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(2), count, "joinSelf");
        }
    }

    @Test
    public void joinSubQuery() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            SubQuery subQuery = SubQuery.create("sub")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .eq(SysRole::getId, 1);

            List<SysUser> list = QueryChain.of(sysUserMapper)
                    .select(subQuery, SysRole::getId, c -> c.as("xx"))
                    .select(subQuery, "id")
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .join(JoinMode.INNER, SysUser.class, subQuery, on -> on.eq(SysUser::getRole_id, subQuery.$().field(subQuery, SysRole::getId)))
                    .eq(subQuery.$(subQuery, SysRole::getId), 1)
                    .orderBy(subQuery, SysRole::getId)
                    .list();
            assertEquals(2, list.size(), "joinSelf");
        }
    }
}
