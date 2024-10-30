package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.vo.SysUserVo;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QueryAsTest extends BaseTest {

    @Test
    public void entityAsTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, c -> c.as(SysUser::getId))
                    .select(SysUser::getUserName, c -> c.as(SysUser::getUserName))
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2)
                    .get();
            System.out.println(sysUser);

            SysUser eqSysUser = new SysUser();
            eqSysUser.setId(2);
            eqSysUser.setUserName("test1");

            assertEquals(eqSysUser, sysUser);
        }
    }


    @Test
    public void voAsTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserVo sysUser = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, c -> c.as(SysUserVo::getId))
                    .select(SysUser::getUserName, c -> c.as(SysUserVo::getUserName))
                    .select(SysUser::getUserName, c -> c.as(SysUserVo::getKkName))
                    .select(SysUser::getUserName, c -> c.as(SysUserVo::getKkName2))
                    .select(SysRole::getId, c -> c.as(SysRole::getId))
                    .select(SysRole::getName, c -> c.as(SysRole::getName))
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .eq(SysUser::getId, 2)
                    .returnType(SysUserVo.class)
                    .get();
            System.out.println(sysUser);

            SysUserVo eqSysUser = new SysUserVo();
            eqSysUser.setId(2);
            eqSysUser.setUserName("test1");
            eqSysUser.setKkName("test1");
            eqSysUser.setKkName2("test1");

            SysRole eqSysRole = new SysRole();
            eqSysRole.setId(1);
            eqSysRole.setName("测试");

            SysRole sysRole = sysUser.getRole();

            sysUser.setRole(null);

            assertEquals(eqSysUser, sysUser);
            assertEquals(eqSysRole, sysRole);
        }
    }

}
