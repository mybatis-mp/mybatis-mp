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

package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.executor.SubQuery;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import db.sql.api.cmd.JoinMode;
import db.sql.api.impl.cmd.dbFun.FunctionInterface;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class JoinTest extends BaseTest {

    @Test
    public void defaultAddOn() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, c -> c.count())
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .returnType(Integer.class)
                    .get();


            assertEquals(Integer.valueOf(2), count, "defaultAddOn");
        }
    }

    @Test
    public void customAddOn() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class, on -> on.eq(SysUser::getRole_id, SysRole::getId).like(SysUser::getUserName, "test1"))
                    .returnType(Integer.class)
                    .get();


            assertEquals(Integer.valueOf(1), count, "customAddOn");
        }
    }

    @Test
    public void innerJoin() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .returnType(Integer.class)
                    .get();
            assertEquals(Integer.valueOf(2), count, "innerJoin");
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Map<String, Object> map = QueryChain.of(sysUserMapper)
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .returnType(Map.class)
                    .orderBy(SysUser::getId)
                    .limit(1)
                    .get();
            assertNotNull(map);
            assertTrue(map instanceof Map);
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<Map<String, Object>> maps = QueryChain.of(sysUserMapper)
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .returnMap()
                    .list();
            System.out.println(maps.get(0).getClass());
            assertEquals(HashMap.class, maps.get(0).getClass());
            assertEquals(3, maps.size());
        }
    }

    @Test
    public void leftJoin() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(JoinMode.LEFT, SysUser.class, SysRole.class)
                    .returnType(Integer.class)
                    .get();
            assertEquals(Integer.valueOf(3), count, "leftJoin");
        }
    }

    @Test
    public void rightJoin() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, c -> c.count())
                    .from(SysUser.class)
                    .join(JoinMode.RIGHT, SysUser.class, SysRole.class)
                    .returnType(Integer.class)
                    .get();
            assertEquals(Integer.valueOf(2), count, "rightJoin");
        }
    }

    @Test
    public void fullJoin() {

        Query query = Query.create()
                .select(SysUser::getId, FunctionInterface::count)
                .from(SysUser.class)
                .join(JoinMode.FULL, SysUser.class, SysRole.class);

        query.setReturnType(Integer.class);
        check("fullJoin", "SELECT  COUNT( t.id) FROM t_sys_user t  FULL OUTER JOIN sys_role t2 ON  t2.id =  t.role_id", query);

    }

    @Test
    public void joinSelf() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Integer count = QueryChain.of(sysUserMapper)
                    .select(SysUser::getId, FunctionInterface::count)
                    .from(SysUser.class)
                    .join(JoinMode.INNER, SysUser.class, 1, SysUser.class, 2, on -> on.eq(SysUser::getId, 1, SysUser::getRole_id, 2))
                    .returnType(Integer.class)
                    .get();
            assertEquals(Integer.valueOf(2), count, "joinSelf");
        }
    }

    @Test
    public void joinSubQuery() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            SubQuery subQuery = SubQuery.create("sub")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .eq(SysRole::getId, 1);

            List<SysUser> list = QueryChain.of(sysUserMapper)
                    .select(subQuery, SysRole::getId, c -> c.as("xx"))
                    .select(subQuery, "id")
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .join(JoinMode.INNER, SysUser.class, subQuery, on -> on.eq(SysUser::getRole_id, subQuery.$outerField(SysRole::getId)))
                    .eq(subQuery.$outerField(SysRole::getId), 1)
                    .orderBy(subQuery, SysRole::getId)
                    .list();
            assertEquals(2, list.size(), "joinSelf");
        }
    }
}
