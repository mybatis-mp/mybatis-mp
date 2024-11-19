package com.mybatis.mp.core.test.testCase.update;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.model.SysUserModel;
import com.mybatis.mp.core.test.testCase.BaseTest;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelNotForceWhereUpdateTest extends BaseTest {

    @Test
    public void notForceInsertTest1() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = QueryChain.of(sysUserMapper).returnType(SysUserModel.class)
                    .eq(SysUser::getId, 1).get();
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, false, where -> where.eq(SysUser::getId, sysUserModel.getId()));
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = QueryChain.of(sysUserMapper).returnType(SysUserModel.class)
                    .eq(SysUser::getId, 1).get();
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, where -> where.eq(SysUser::getId, sysUserModel.getId()));
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = QueryChain.of(sysUserMapper).returnType(SysUserModel.class)
                    .eq(SysUser::getId, 1).get();
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, (Consumer<Where>) where -> where.eq(SysUser::getId, sysUserModel.getId()), SysUserModel::getRole_id);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }

    @Test
    public void notForceInsertTest4() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = QueryChain.of(sysUserMapper).returnType(SysUserModel.class)
                    .eq(SysUser::getId, 1).get();
            sysUserModel.setUserName(null);
            sysUserMapper.update(sysUserModel, WhereUtil.create().eq(SysUser::getId, sysUserModel.getId()), SysUserModel::getRole_id);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), "admin");
            assertEquals(sysUser.getRole_id(), 0);
        }
    }
}
