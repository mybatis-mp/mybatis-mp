package com.mybatis.mp.core.test.testCase.update;

import cn.mybatis.mp.core.sql.util.WhereUtil;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EntityForceWhereUpdateTest extends BaseTest {

    @Test
    public void forceInsertTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, true, where -> where.eq(SysUser::getId, sysUserModel.getId()));
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void forceInsertTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, (Consumer<Where>) where -> where.eq(SysUser::getId, sysUserModel.getId()), SysUser::getUserName);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 0);
        }
    }


    @Test
    public void forceInsertTest3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, true, WhereUtil.create().eq(SysUser::getId, sysUserModel.getId()));
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void forceInsertTest4() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUser sysUserModel = sysUserMapper.getById(1);
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, WhereUtil.create().eq(SysUser::getId, sysUserModel.getId()), SysUser::getUserName);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

}
