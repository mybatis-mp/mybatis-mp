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

package com.mybatis.mp.core.test.testCase.multiPk;

import com.mybatis.mp.core.test.DO.MultiPk;
import com.mybatis.mp.core.test.mapper.MultiPkMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.basic.TableField;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiPkTestCase extends BaseTest {

    private MultiPk getById(MultiPkMapper mapper, Integer id1, Integer id2) {
        return mapper.get(where -> {
            where.eq(MultiPk::getId1, id1).eq(MultiPk::getId2, id2);
        });
    }

    @Test
    public void saveBatchTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper mapper = session.getMapper(MultiPkMapper.class);

            MultiPk entity = new MultiPk();
            entity.setId1(1);
            entity.setId2(2);
            entity.setName("12");
            mapper.saveBatch(Collections.singletonList(entity));
        }
    }

    @Test
    public void saveBatchConflictTest() {
        if (TestDataSource.DB_TYPE != DbType.MYSQL && TestDataSource.DB_TYPE != DbType.MARIA_DB
                && TestDataSource.DB_TYPE != DbType.PGSQL
                && TestDataSource.DB_TYPE != DbType.KING_BASE
                && TestDataSource.DB_TYPE != DbType.OPEN_GAUSS) {
            return;
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper mapper = session.getMapper(MultiPkMapper.class);

            MultiPk entity = new MultiPk();
            entity.setId1(1);
            entity.setId2(2);
            entity.setName("12");
            mapper.saveBatch(Collections.singletonList(entity));
            mapper.saveBatch(Collections.singletonList(entity), c -> {
                c.listen(baseInsert -> baseInsert
                        .conflictKeys(MultiPk::getId1, MultiPk::getId2)
                        .onConflictAction(true)
                        .onConflictAction(update -> update.set(MultiPk::getName, (java.util.function.Function<TableField, Cmd>) xxx -> xxx.concat(1)))
                );
            });
        }
    }

    @Test
    public void saveOrUpdateTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper mapper = session.getMapper(MultiPkMapper.class);

            MultiPk entity = new MultiPk();
            entity.setId1(1);
            entity.setId2(2);
            entity.setName("12");
            mapper.saveOrUpdate(Collections.singletonList(entity));
            mapper.saveOrUpdate(Collections.singletonList(entity));
        }
    }

    @Test
    public void oneTest() {

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper mapper = session.getMapper(MultiPkMapper.class);
            MultiPk entity = new MultiPk();
            entity.setId1(1);
            entity.setId2(2);
            entity.setName("12");
            mapper.save(entity);

            assertEquals(entity, getById(mapper, 1, 2));
            entity.setName("12update");
            mapper.update(entity);
            mapper.saveOrUpdate(entity);

            assertEquals("12update", getById(mapper, 1, 2).getName());
            mapper.delete(entity);

            assertEquals(null, getById(mapper, 1, 2));
        }

    }

    @Test
    public void multiTest() {

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper mapper = session.getMapper(MultiPkMapper.class);
            MultiPk entity = new MultiPk();
            entity.setId1(1);
            entity.setId2(2);
            entity.setName("12");
            mapper.saveOrUpdate(entity);

            assertEquals(entity, getById(mapper, 1, 2));
            entity.setName("12update");

            List<MultiPk> list = Arrays.asList(entity);
            mapper.update(list);

            assertEquals("12update", getById(mapper, 1, 2).getName());

            mapper.delete(list);
            assertEquals(null, getById(mapper, 1, 2));
        }

    }
}
