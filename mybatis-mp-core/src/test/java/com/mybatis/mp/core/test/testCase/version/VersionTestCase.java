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

package com.mybatis.mp.core.test.testCase.version;

import com.mybatis.mp.core.test.DO.VersionTest;
import com.mybatis.mp.core.test.mapper.VersionTestMapper;
import com.mybatis.mp.core.test.model.VersionModel;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VersionTestCase extends BaseTest {

    @Test
    public void insertTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            VersionTestMapper versionTestMapper = session.getMapper(VersionTestMapper.class);
            VersionTest versionTest = new VersionTest();
            versionTest.setName("我是1");
            versionTest.setCreateTime(LocalDateTime.now());
            versionTestMapper.save(versionTest);
            System.out.println(versionTest);
            assertNotNull(versionTest.getId());
            assertEquals(1, (int) versionTestMapper.getById(versionTest.getId()).getVersion());
        }
    }

    @Test
    public void updateTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            VersionTestMapper versionTestMapper = session.getMapper(VersionTestMapper.class);
            VersionTest versionTest = new VersionTest();
            versionTest.setName("我是1");
            versionTest.setCreateTime(LocalDateTime.now());
            versionTestMapper.save(versionTest);

            versionTest.setName("我是2");
            versionTestMapper.update(versionTest);
            versionTest = versionTestMapper.getById(versionTest.getId());

            System.out.println(versionTest);
            assertEquals(2, (int) versionTest.getVersion());
            assertEquals(2, (int) versionTestMapper.getById(versionTest.getId()).getVersion());
        }
    }

    @Test
    public void insertWithModelTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            VersionTestMapper versionTestMapper = session.getMapper(VersionTestMapper.class);
            VersionModel versionTest = new VersionModel();
            versionTest.setName("我是1");
            versionTest.setCreateTime(LocalDateTime.now());
            versionTestMapper.save(versionTest);
            System.out.println(versionTest);
            assertNotNull(versionTest.getId());
            assertEquals(1, (int) versionTestMapper.getById(versionTest.getId()).getVersion());
        }
    }

    @Test
    public void updateWithModelTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            VersionTestMapper versionTestMapper = session.getMapper(VersionTestMapper.class);
            VersionModel versionTest = new VersionModel();
            versionTest.setName("我是1");
            versionTest.setCreateTime(LocalDateTime.now());
            versionTestMapper.save(versionTest);
            System.out.println(versionTest);

            versionTest.setName("我是2");
            versionTestMapper.update(versionTest);
            VersionTest versionTest2 = versionTestMapper.getById(versionTest.getId());

            System.out.println(versionTest);
            assertEquals(2, (int) versionTest2.getVersion());
            assertEquals(2, (int) versionTestMapper.getById(versionTest2.getId()).getVersion());
        }
    }
}
