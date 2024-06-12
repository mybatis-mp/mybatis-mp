package com.mybatis.mp.core.test.testCase.cmdTemplete;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import com.mybatis.mp.core.test.vo.SysUserRoleAutoSelectVo;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.basic.CmdTemplate;
import db.sql.api.impl.cmd.basic.ConditionTemplate;
import db.sql.api.impl.cmd.basic.FunTemplate;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CmdTemplateTestCase extends BaseTest {

    @Test
    public void templateTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            String str = QueryChain.of(sysUserMapper)
                    .selectWithFun(SysUser::getRole_id, c -> CmdTemplate.create("count({0})+{1}", c, 1).as("cnt"))
                    .from(SysUser.class)
                    .and(cs -> ConditionTemplate.create("{0}+{1}={2}", cs[0], cs[1], 2).as("xx"), SysUser::getId, SysUser::getId)
                    .returnType(String.class)
                    .get();

            assertTrue(str.equals("2") || str.equals("2.0"));
        }
    }


    @Test
    public void templateTest2() {
        if (TestDataSource.DB_TYPE == DbType.ORACLE || TestDataSource.DB_TYPE == DbType.SQL_SERVER) {
            return;
        }
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            String str = QueryChain.of(sysUserMapper)
                    .selectWithFun(SysUser::getRole_id, c -> FunTemplate.create("count({0})", c).as("xx").plus(1).concat(1, "2", 3).length())
                    .from(SysUser.class)
                    .and(cs -> ConditionTemplate.create("{0}+{1}={2}", cs[0], cs[1], 2).as("xx2"), SysUser::getId, SysUser::getId)
                    .returnType(String.class)
                    .get();

            assertTrue(str.equals("4") || str.equals("4.0"));
        }
    }

    @Test
    public void templateTest3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserRoleAutoSelectVo vo = QueryChain.of(sysUserMapper)
                    .select(SysUserRoleAutoSelectVo.class)
                    .selectWithFun(SysRole::getId, c -> CmdTemplate.create(" RANK() OVER( ORDER BY {0}) ", c).as("RANK2"))
                    .selectWithFun(SysRole::getId, c -> CmdTemplate.create(" RANK() OVER( ORDER BY {0}) as RANK3", c))
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .returnType(SysUserRoleAutoSelectVo.class)
                    .orderBy(SysUser::getId)
                    .limit(1)
                    .get();

            assertEquals(vo.getId(), 2);
        }
    }
}
