package com.mybatis.mp.core.test.testCase.update;

import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityListNotForceUpdateTest extends BaseTest {

    @Test
    public void notForceInsertTest1() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.update(Collections.singletonList(sysUserModel), false);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.update(Collections.singletonList(sysUserModel));
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.update(Collections.singletonList(sysUserModel), SysUser::getRole_id);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest4() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveOrUpdate(Collections.singletonList(sysUserModel), false);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest5() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveOrUpdate(Collections.singletonList(sysUserModel));
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest6() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveOrUpdate(Collections.singletonList(sysUserModel), SysUser::getId);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }
}
