package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.DO.SysUserEncrypt;
import com.mybatis.mp.core.test.mapper.SysRoleMapper;
import com.mybatis.mp.core.test.mapper.SysUserEncryptMapper;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import com.mybatis.mp.core.test.vo.JsonTypeTestVo;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.tookit.SQLPrinter;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class TypeHandlerTest extends BaseTest {
    @Test
    public void json() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            JsonTypeTestVo jsonTypeTestVo = QueryChain.of(sysUserMapper)
                    .select(Methods.value("[{\"id\":123}]").as("aa"))
                    .select(Methods.value("{\"id\":1234}").as("bb"))
                    .from(SysUser.class)
                    .eq(SysUser::getId, 1)
                    .returnType(JsonTypeTestVo.class)
                    .get();

            assertEquals(jsonTypeTestVo.getAa().get(0).getClass(), SysUser.class);
            assertEquals(jsonTypeTestVo.getAa().get(0).getId(), 123);
            assertEquals(jsonTypeTestVo.getBb().getId(), 1234);

            assertEquals(jsonTypeTestVo.getDd().get(0).getClass(), SysRole.class);
            assertEquals(jsonTypeTestVo.getDd().get(0).getId(), 123);
            assertEquals(jsonTypeTestVo.getEe().getId(), 1234);
        }
    }

    @Test
    public void json2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            QueryChain queryChain = QueryChain.of(sysRoleMapper)
                    .select(Methods.value("[{\"id\":123}]").as("aa"))
                    .select(Methods.value("{\"id\":1234}").as("bb"))
                    .from(SysRole.class)
                    .eq(SysRole::getId, 1);

            String sql = SQLPrinter.sql(TestDataSource.DB_TYPE, queryChain);

            JsonTypeTestVo jsonTypeTestVo = sysRoleMapper.jsonTypeTest1(sql);

            assertEquals(jsonTypeTestVo.getAa().get(0).getClass(), SysUser.class);
            assertEquals(jsonTypeTestVo.getAa().get(0).getId(), 123);
            assertEquals(jsonTypeTestVo.getBb().getId(), 1234);

            assertEquals(jsonTypeTestVo.getDd().get(0).getClass(), SysRole.class);
            assertEquals(jsonTypeTestVo.getDd().get(0).getId(), 123);
            assertEquals(jsonTypeTestVo.getEe().getId(), 1234);
        }
    }

    @Test
    public void json3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            QueryChain queryChain = QueryChain.of(sysRoleMapper)
                    .select(Methods.value("[{\"id\":123}]").as("aa"))
                    .select(Methods.value("{\"id\":1234}").as("bb"))
                    .from(SysRole.class)
                    .eq(SysRole::getId, 1);

            String sql = SQLPrinter.sql(TestDataSource.DB_TYPE, queryChain);

            JsonTypeTestVo jsonTypeTestVo = sysRoleMapper.jsonTypeTest2(sql);

            assertEquals(jsonTypeTestVo.getAa().get(0).getClass(), SysUser.class);
            assertEquals(jsonTypeTestVo.getAa().get(0).getId(), 123);
            assertEquals(jsonTypeTestVo.getBb().getId(), 1234);

            assertEquals(jsonTypeTestVo.getDd().get(0).getClass(), SysRole.class);
            assertEquals(jsonTypeTestVo.getDd().get(0).getId(), 123);
            assertEquals(jsonTypeTestVo.getEe().getId(), 1234);
        }
    }

    @Test
    public void likeQueryTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserEncryptMapper sysUserMapper = session.getMapper(SysUserEncryptMapper.class);

            SysUserEncrypt sysUser = new SysUserEncrypt();
            if (TestDataSource.DB_TYPE != DbType.SQL_SERVER) {
                sysUser.setId(123);
            }

            sysUser.setUserName("11111");
            sysUser.setRole_id(1);
            sysUser.setPassword("123");
            sysUser.setCreate_time(LocalDateTime.now());

            sysUserMapper.save(sysUser);

            sysUser = sysUserMapper.get(where -> where
                    .eq(SysUserEncrypt::getUserName, "1111")
            );

            assertEquals(sysUser.getUserName(), "加密后的数据");

            sysUser = sysUserMapper.get(where -> where
                    .like(SysUserEncrypt::getUserName, "加密参数")
            );

            assertNull(sysUser);
        }
    }
}
