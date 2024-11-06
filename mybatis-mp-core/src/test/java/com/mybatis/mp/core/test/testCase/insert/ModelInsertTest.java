package com.mybatis.mp.core.test.testCase.insert;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.model.SysUserModel;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelInsertTest extends BaseTest {

    @Test
    public void forceInsertTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.save(sysUserModel,true);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
        }
    }

    @Test
    public void forceInsertTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.save(sysUserModel,false);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
        }
    }

    @Test
    public void batchForceInsertTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveModels(Collections.singletonList(sysUserModel),true);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
        }
    }

    @Test
    public void batchForceInsertTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveModels(Collections.singletonList(sysUserModel),false);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
        }
    }
}
