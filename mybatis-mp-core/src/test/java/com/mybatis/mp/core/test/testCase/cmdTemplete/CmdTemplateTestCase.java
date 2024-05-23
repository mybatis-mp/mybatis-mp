package com.mybatis.mp.core.test.testCase.cmdTemplete;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.basic.CmdTemplate;
import db.sql.api.impl.cmd.basic.ConditionTemplate;
import db.sql.api.impl.cmd.basic.FunTemplate;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class CmdTemplateTestCase extends BaseTest {

    @Test
    public void templateTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            QueryChain queryChain = QueryChain.of(sysUserMapper);
            queryChain.selectWithFun(SysUser::getRole_id, c -> CmdTemplate.create("count({0})+{1}", c, 1));
            queryChain.from(SysUser.class);
            queryChain.and(cs -> ConditionTemplate.create("{0}+{1}={2}", cs[0], cs[1], 2), SysUser::getId, SysUser::getId);
            queryChain.returnType(String.class);
            String str = queryChain.get();

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
            QueryChain queryChain = QueryChain.of(sysUserMapper);
            queryChain.selectWithFun(SysUser::getRole_id, c -> FunTemplate.create("count({0})", c).plus(1).concat(1, "2", 3).length());
            queryChain.from(SysUser.class);
            queryChain.and(cs -> ConditionTemplate.create("{0}+{1}={2}", cs[0], cs[1], 2), SysUser::getId, SysUser::getId);
            queryChain.returnType(String.class);
            String str = queryChain.get();

            assertTrue(str.equals("4") || str.equals("4.0"));
        }
    }
}
