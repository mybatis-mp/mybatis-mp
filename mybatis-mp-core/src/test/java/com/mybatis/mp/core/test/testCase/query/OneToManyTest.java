package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.db.reflect.ResultInfos;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysRoleMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.vo.OneToManyTypeHandlerVo;
import com.mybatis.mp.core.test.vo.OneToManyVo;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OneToManyTest extends BaseTest {


    @Test
    public void oneToManyTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            List<OneToManyVo> list = QueryChain.of(sysRoleMapper)
                    .select(SysUser.class)
                    .selectWithFun(SysUser::getUserName, c -> c.as(OneToManyVo::getAsName))
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .join(SysRole.class, SysUser.class, on -> on.eq(SysUser::getRole_id, SysRole::getId))
                    .returnType(OneToManyVo.class)
                    .list();


            System.out.println(ResultInfos.get(OneToManyVo.class).getTablePrefixes());

            System.out.println(list);
            assertEquals(OneToManyVo.class, list.get(0).getClass(), "oneToManyTest");
            assertEquals(Integer.valueOf(1), list.size(), "oneToManyTest");
            assertEquals(Integer.valueOf(2), list.get(0).getSysUserList().size(), "oneToManyTest");
            assertEquals(SysUser.class, list.get(0).getSysUserList().get(0).getClass(), "oneToManyTest");

            assertEquals(list.get(0).getAsName(), "test1");
        }
    }


    @Test
    public void oneToManyTypeHandlerTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            List<OneToManyTypeHandlerVo> list = QueryChain.of(sysRoleMapper)
                    .select(SysUser.class)
                    .selectWithFun(SysUser::getUserName, c -> c.as("kk"))
                    .selectWithFun(SysUser::getUserName, c -> c.as("kk2"))
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .join(SysRole.class, SysUser.class, on -> on.eq(SysUser::getRole_id, SysRole::getId))
                    .returnType(OneToManyTypeHandlerVo.class)
                    .list();

            System.out.println(list);
            assertEquals("xxxx123", list.get(0).getName());
            assertEquals("xxxx123", list.get(0).getKkName());
            assertEquals("xxxx123", list.get(0).getSysUserList().get(0).getUserName());
            assertEquals("xxxx123", list.get(0).getSysUserList().get(0).getPwd());
            assertEquals("null-phone", list.get(0).getSysUserList().get(1).getPwd());
            assertEquals("xxxx123", list.get(0).getSysUserList().get(0).getKkName());
        }
    }
}
