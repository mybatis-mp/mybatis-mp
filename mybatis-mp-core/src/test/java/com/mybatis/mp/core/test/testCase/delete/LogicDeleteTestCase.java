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

package com.mybatis.mp.core.test.testCase.delete;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.LogicDeleteTest;
import com.mybatis.mp.core.test.mapper.LogicDeleteTestMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogicDeleteTestCase extends BaseTest {

    public LogicDeleteTestCase() {
        MybatisMpConfig.setLogicDeleteSwitch(true);
    }

    @Test
    public void deleteIdTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            LogicDeleteTestMapper logicDeleteTestMapper = session.getMapper(LogicDeleteTestMapper.class);
            QueryChain.of(logicDeleteTestMapper).list();
            logicDeleteTestMapper.deleteById(1);
            List<LogicDeleteTest> list = QueryChain.of(logicDeleteTestMapper).list();
            assertEquals(list.size(), 2);

            QueryChain.of(logicDeleteTestMapper).join(LogicDeleteTest.class, 1, LogicDeleteTest.class, 2, on -> on.eq(LogicDeleteTest::getId, 1, LogicDeleteTest::getId, 2)).list();
        }
    }


    @Test
    public void deleteIdTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            LogicDeleteTestMapper logicDeleteTestMapper = session.getMapper(LogicDeleteTestMapper.class);
            logicDeleteTestMapper.deleteById(1);

            LogicDeleteTest logicDeleteTest = LogicDeleteUtil.execute(false, () -> logicDeleteTestMapper.getById(1));
            assertEquals(logicDeleteTest.getDeleted(), Byte.valueOf("1"));
        }
    }

    @Test
    public void deleteEntityTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            LogicDeleteTestMapper logicDeleteTestMapper = session.getMapper(LogicDeleteTestMapper.class);
            logicDeleteTestMapper.delete(logicDeleteTestMapper.getById(1));
            List<LogicDeleteTest> list = QueryChain.of(logicDeleteTestMapper).list();
            assertEquals(list.size(), 2);
        }
    }


    @Test
    public void deleteWithWhereTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            LogicDeleteTestMapper logicDeleteTestMapper = session.getMapper(LogicDeleteTestMapper.class);
            logicDeleteTestMapper.delete(where -> where.eq(LogicDeleteTest::getId, 1));
            List<LogicDeleteTest> list = QueryChain.of(logicDeleteTestMapper).list();
            assertEquals(list.size(), 2);
        }
    }

}
