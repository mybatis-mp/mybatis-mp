package com.mybatis.mp.core.test.testCase;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.BaseIDSysUser;
import com.mybatis.mp.core.test.DO.BaseRoleIdSysUserVo;
import com.mybatis.mp.core.test.DO.BaseRoleIdSysUserVo2;
import com.mybatis.mp.core.test.mapper.SysUserIDMapper;
import com.mybatis.mp.core.test.model.BaseIDSysUserModel;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseUserTestCase extends BaseTest {

    private void save(SqlSession session, BaseIDSysUser baseIDSysUser) {
        SysUserIDMapper sysUserMapper = session.getMapper(SysUserIDMapper.class);
        sysUserMapper.save(baseIDSysUser);
    }

    private void save(SqlSession session, BaseIDSysUserModel baseIDSysUser) {
        SysUserIDMapper sysUserMapper = session.getMapper(SysUserIDMapper.class);
        sysUserMapper.save(baseIDSysUser);
    }

    @Test
    public void save() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            BaseIDSysUser baseIDSysUser = new BaseIDSysUser();
            baseIDSysUser.setUserName("aaa2");
            baseIDSysUser.setRole_id(1);
            baseIDSysUser.setPassword("xx");
            baseIDSysUser.setCreate_time(LocalDateTime.now());
            for (int i = 0; i < 100; i++) {
                try {
                    save(this.sqlSessionFactory.openSession(false), baseIDSysUser);
                    break;
                } catch (Exception e) {
                    System.out.println(i);
                }
            }

            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            assertEquals(baseIDSysUser.getId().getClass(), Long.class);
        }
    }

    @Test
    public void update() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserIDMapper sysUserMapper = session.getMapper(SysUserIDMapper.class);
            BaseIDSysUser baseIDSysUser = new BaseIDSysUser();
            baseIDSysUser.setId(1L);
            baseIDSysUser.setUserName("aaa2");
            baseIDSysUser.setRole_id(1);
            baseIDSysUser.setPassword("xx");
            baseIDSysUser.setCreate_time(LocalDateTime.now());
            sysUserMapper.update(baseIDSysUser);
            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            assertEquals(baseIDSysUser.getId().getClass(), Long.class);
        }
    }

    @Test
    public void saveModel() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserIDMapper sysUserMapper = session.getMapper(SysUserIDMapper.class);
            BaseIDSysUserModel baseIDSysUser = new BaseIDSysUserModel();
            baseIDSysUser.setUserName("aaa2");
            baseIDSysUser.setRole_id(1);
            baseIDSysUser.setPassword("xx");
            baseIDSysUser.setCreate_time(LocalDateTime.now());
            for (int i = 0; i < 100; i++) {
                try {
                    save(this.sqlSessionFactory.openSession(false), baseIDSysUser);
                    break;
                } catch (Exception e) {
                    System.out.println(i);
                }
            }

            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            assertEquals(baseIDSysUser.getId().getClass(), Long.class);
        }
    }

    @Test
    public void updateModel() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserIDMapper sysUserMapper = session.getMapper(SysUserIDMapper.class);
            BaseIDSysUserModel baseIDSysUser = new BaseIDSysUserModel();
            baseIDSysUser.setId(1L);
            baseIDSysUser.setUserName("aaa2");
            baseIDSysUser.setRole_id(1);
            baseIDSysUser.setPassword("xx");
            baseIDSysUser.setCreate_time(LocalDateTime.now());
            sysUserMapper.update(baseIDSysUser);
            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            assertEquals(baseIDSysUser.getId().getClass(), Long.class);
        }
    }

    @Test
    public void test1() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserIDMapper sysUserMapper = session.getMapper(SysUserIDMapper.class);
            BaseIDSysUser baseIDSysUser = QueryChain.of(sysUserMapper)
                    .from(BaseIDSysUser.class)
                    .eq(BaseIDSysUser::getId, 2)
                    .get();

            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            assertEquals(baseIDSysUser.getId(), 2);
        }
    }

    @Test
    public void test2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserIDMapper sysUserMapper = session.getMapper(SysUserIDMapper.class);
            BaseRoleIdSysUserVo baseRoleIdSysUserVo = QueryChain.of(sysUserMapper)
                    .from(BaseIDSysUser.class)
                    .eq(BaseIDSysUser::getId, 2)
                    .returnType(BaseRoleIdSysUserVo.class)
                    .get();

            System.out.println(baseRoleIdSysUserVo);
            System.out.println(baseRoleIdSysUserVo.getId().getClass());
            assertEquals(baseRoleIdSysUserVo.getId(), 2);
            assertEquals(baseRoleIdSysUserVo.getRole_id(), "1");

            BaseIDSysUser baseIDSysUser = baseRoleIdSysUserVo.getBaseIDSysUser();
            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            assertEquals(baseIDSysUser.getId(), 2);

            baseIDSysUser = baseRoleIdSysUserVo.getBaseIDSysUser2();
            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            assertEquals(baseIDSysUser.getId(), 2);


            baseIDSysUser = baseRoleIdSysUserVo.getBaseIDSysUsers().get(0);
            System.out.println(baseIDSysUser);
            System.out.println(baseIDSysUser.getId().getClass());
            System.out.println(baseIDSysUser.getRole_id().getClass());
            assertEquals(baseIDSysUser.getId(), 2);

            BaseRoleIdSysUserVo2 baseRoleIdSysUserVo2 = baseRoleIdSysUserVo.getBaseRoleIdSysUserVo();
            System.out.println(baseRoleIdSysUserVo2.getId().getClass());
            System.out.println(baseRoleIdSysUserVo2.getRole_id().getClass());
            assertEquals(baseRoleIdSysUserVo2.getId(), 2);
            assertEquals(baseRoleIdSysUserVo2.getRole_id(), "1");


            baseRoleIdSysUserVo2 = baseRoleIdSysUserVo.getBaseRoleIdSysUserVo2();
            System.out.println(baseRoleIdSysUserVo2.getId().getClass());
            System.out.println(baseRoleIdSysUserVo2.getRole_id().getClass());
            assertEquals(baseRoleIdSysUserVo2.getId(), 2);
            assertEquals(baseRoleIdSysUserVo2.getRole_id(), "1");

        }
    }
}
