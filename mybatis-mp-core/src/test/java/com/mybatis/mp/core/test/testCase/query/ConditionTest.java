package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import db.sql.api.cmd.LikeMode;
import db.sql.api.impl.exception.ConditionValueNullException;
import db.sql.api.impl.tookit.Objects;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class ConditionTest extends BaseTest {

    @Test
    public void empty() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 1, Objects::nonNull)
                    .empty(SysUser::getUserName)
                    .returnType(Integer.TYPE)
                    .get();

            assertNull(id, "eq");
        }
    }

    @Test
    public void notEmpty() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2)
                    .notEmpty(SysUser::getUserName)
                    .returnType(Integer.TYPE)
                    .get();

            assertEquals(Integer.valueOf(2), id, "eq");
        }
    }

    @Test
    public void eq() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2)
                    .returnType(Integer.TYPE)
                    .get();

            assertEquals(Integer.valueOf(2), id, "eq");
        }
    }

    @Test
    public void wandOr() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .selectCountAll()
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2).and().or().eq(SysUser::getId, 1)
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(2), count, "andOr");
        }
    }

    @Test
    public void ne() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .ne(SysUser::getId, 2)
                    .like(SysUser::getUserName, "test")
                    .returnType(Integer.TYPE)
                    .get();

            assertEquals(Integer.valueOf(3), id, "eq");
        }
    }

    @Test
    public void gt() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .gt(SysUser::getId, 1)
                    .orderBy(SysUser::getId)
                    .limit(1)
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(2), id, "gt");
        }
    }

    @Test
    public void gte() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .gte(SysUser::getId, 2)
                    .orderBy(SysUser::getId)
                    .limit(1)
                    .returnType(Integer.TYPE)
                    .get();

            assertEquals(Integer.valueOf(2), id, "gte");
        }
    }

    @Test
    public void lt() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .lt(SysUser::getId, 2)
                    .orderBy(SysUser::getId)
                    .limit(1)
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(1), id, "lt");
        }
    }

    @Test
    public void lte() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer id = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .lte(SysUser::getId, 1)
                    .orderBy(SysUser::getId)
                    .limit(1)
                    .returnType(Integer.TYPE)
                    .get();
            assertEquals(Integer.valueOf(1), id, "lte");
        }
    }

    @Test
    public void isNotNull() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, SysUser::getPassword)
                    .from(SysUser.class)
                    .isNotNull(SysUser::getPassword)
                    .eq(SysUser::getId, 3)
                    .get();
            assertNull(sysUser, "isNotNull");
        }
    }

    @Test
    public void isNull() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
                    .from(SysUser.class)
                    .isNull(SysUser::getPassword)
                    .get();

            SysUser eqSysUser = new SysUser();
            eqSysUser.setId(3);
            eqSysUser.setPassword(null);
            eqSysUser.setRole_id(1);
            assertEquals(eqSysUser, sysUser, "isNull");
        }
    }

    @Test
    public void like() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
                    .from(SysUser.class)
                    .like(SysUser::getUserName, "test1")
                    .get();


            SysUser eqSysUser = new SysUser();
            eqSysUser.setId(2);
            eqSysUser.setPassword("123456");
            eqSysUser.setRole_id(1);
            assertEquals(eqSysUser, sysUser, "like");
        }
    }

    @Test
    public void rightLike() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .selectWithFun(SysUser::getId, c -> c.count())
                    .from(SysUser.class)
                    .like(LikeMode.RIGHT, SysUser::getUserName, "test")
                    .returnType(Integer.TYPE)
                    .count();


            assertEquals(Integer.valueOf(2), count, "rightLike");
        }
    }

    @Test
    public void leftLike() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
                    .from(SysUser.class)
                    .like(LikeMode.LEFT, SysUser::getUserName, "test1")
                    .get();


            SysUser eqSysUser = new SysUser();
            eqSysUser.setId(2);
            eqSysUser.setPassword("123456");
            eqSysUser.setRole_id(1);
            assertEquals(eqSysUser, sysUser, "leftLike");
        }
    }

    @Test
    public void notLike() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
                    .from(SysUser.class)
                    .notLike(SysUser::getUserName, "test1")
                    .like(SysUser::getUserName, "test")
                    .get();


            SysUser eqSysUser = new SysUser();
            eqSysUser.setId(3);
            eqSysUser.setPassword(null);
            eqSysUser.setRole_id(1);
            assertEquals(eqSysUser, sysUser, "notLike");
        }
    }

    @Test
    public void notRightLike() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
                    .from(SysUser.class)
                    .notLike(LikeMode.RIGHT, SysUser::getUserName, "test2")
                    .like(SysUser::getUserName, "test")
                    .get();


            SysUser eqSysUser = new SysUser();
            eqSysUser.setId(2);
            eqSysUser.setPassword("123456");
            eqSysUser.setRole_id(1);
            assertEquals(eqSysUser, sysUser, "notRightLike");
        }
    }

    @Test
    public void notLeftLike() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, SysUser::getPassword, SysUser::getRole_id)
                    .from(SysUser.class)
                    .notLike(LikeMode.LEFT, SysUser::getUserName, "est1")
                    .like(SysUser::getUserName, "test")
                    .get();

            SysUser eqSysUser = new SysUser();
            eqSysUser.setId(3);
            eqSysUser.setPassword(null);
            eqSysUser.setRole_id(1);
            assertEquals(eqSysUser, sysUser, "notLeftLike");
        }
    }

    @Test
    public void between() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<Integer> list = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .between(SysUser::getId, 1, 2)
                    .returnType(Integer.TYPE)
                    .list();


            assertEquals(Integer.valueOf(1), list.get(0), "between");
            assertEquals(Integer.valueOf(2), list.get(1), "between");
        }
    }

    @Test
    public void notBetween() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<Integer> list = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .between(SysUser::getId, 1, 3)
                    .notBetween(SysUser::getId, 1, 2)
                    .returnType(Integer.TYPE)
                    .list();
            assertEquals(1, list.size(), "notLeftLike");
            assertEquals(Integer.valueOf(3), list.get(0), "notLeftLike");
        }
    }

    @Test
    public void in() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<Integer> list = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .in(SysUser::getId, 1, 2)
                    .in(true, SysUser::getId, 1, 2)
                    .in(SysUser::getId, Arrays.asList(1, 2))
                    .in(SysUser::getId, Arrays.asList(1, 2).stream().collect(Collectors.toSet()))
                    .in(true, SysUser::getId, Arrays.asList(1, 2))
                    .returnType(Integer.TYPE)
                    .list();

            assertEquals(2, list.size());
            assertEquals(Integer.valueOf(1), list.get(0));
            assertEquals(Integer.valueOf(2), list.get(1));
        }
    }

    @Test
    public void notIn() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<Integer> list = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .in(SysUser::getId, 1, 2)
                    .in(true, SysUser::getId, 1, 2)
                    .in(SysUser::getId, Arrays.asList(1, 2))
                    .in(true, SysUser::getId, Arrays.asList(1, 2))

                    .notIn(SysUser::getId, 5)
                    .notIn(true, SysUser::getId, 5, 6)
                    .notIn(SysUser::getId, Arrays.asList(5, 6))
                    .notIn(true, SysUser::getId, Arrays.asList(5, 6))
                    .returnType(Integer.TYPE)
                    .list();

            assertEquals(2, list.size());
            assertEquals(Integer.valueOf(1), list.get(0));
            assertEquals(Integer.valueOf(2), list.get(1));
        }
    }

    @Test
    public void ignoreNullTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<Integer> list = QueryChain.of(sysUserMapper)
                    .forSearch()
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .in(SysUser::getId, 1, 2, null)
                    .returnType(Integer.TYPE)
                    .list();


            assertEquals(Integer.valueOf(1), list.get(0), "between");
            assertEquals(Integer.valueOf(2), list.get(1), "between");
        }
    }

    @Test
    public void notIgnoreNullTest() {
        assertThrows(ConditionValueNullException.class, () -> {
            try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
                SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
                List<Integer> list = QueryChain.of(sysUserMapper)
                        .select(SysUser::getId)
                        .from(SysUser.class)
                        .in(SysUser::getId, 1, 2, null)
                        .returnType(Integer.TYPE)
                        .list();
            }
        });
    }

    @Test
    public void ignoreEmptyTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .forSearch()
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 1)
                    .eq(SysUser::getUserName, "")
                    .eq(SysUser::getUserName, " ")
                    .returnType(SysUser.class)
                    .get();
            assertNotNull(sysUser);
        }
    }

    @Test
    public void notIgnoreEmptyTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 1)
                    .eq(SysUser::getUserName, "")
                    .returnType(SysUser.class)
                    .get();
            assertNull(sysUser);
        }
    }

    @Test
    public void stringTrimTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .forSearch()
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getUserName, " admin ")
                    .returnType(SysUser.class)
                    .get();
            assertNotNull(sysUser);
        }
    }

    @Test
    public void notStringTrimTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .trimStringInCondition(false)
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getUserName, " admin ")
                    .returnType(SysUser.class)
                    .get();
            assertNull(sysUser);
        }
    }
}
