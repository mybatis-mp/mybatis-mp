/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.mybatis.mp.core.test.testCase.insert;

import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.model.SysUserModel;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import db.sql.api.DbType;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ModelListForceInsertTest extends BaseTest {

    @Test
    public void batchForceInsertTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(TestDataSource.DB_TYPE == DbType.SQL_SERVER || TestDataSource.DB_TYPE == DbType.DB2 ? null : 100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveModel(Collections.singletonList(sysUserModel), true);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 1);
        }
    }

    @Test
    public void batchForceInsertTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(TestDataSource.DB_TYPE == DbType.SQL_SERVER || TestDataSource.DB_TYPE == DbType.DB2 ? null : 100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveModel(Collections.singletonList(sysUserModel), SysUserModel::getUserName);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 1);
        }
    }

    @Test
    public void batchForceInsertTest3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(TestDataSource.DB_TYPE == DbType.SQL_SERVER || TestDataSource.DB_TYPE == DbType.DB2 ? null : 100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveOrUpdateModel(Collections.singletonList(sysUserModel), true);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 1);
        }
    }

    @Test
    public void batchForceInsertTest4() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysUserModel sysUserModel = new SysUserModel();
            sysUserModel.setId(TestDataSource.DB_TYPE == DbType.SQL_SERVER || TestDataSource.DB_TYPE == DbType.DB2 ? null : 100);
            sysUserModel.setPassword("!23");
            sysUserModel.setCreate_time(LocalDateTime.now());
            sysUserModel.setRole_id(1);
            sysUserModel.setUserName(null);
            sysUserMapper.saveOrUpdateModel(Collections.singletonList(sysUserModel), SysUserModel::getUserName);
            SysUser sysUser = sysUserMapper.getById(sysUserModel.getId());
            assertEquals(sysUser.getUserName(), null);
            assertEquals(sysUser.getRole_id(), 1);
        }
    }
}
