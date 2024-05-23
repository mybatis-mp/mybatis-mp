package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.mapper.SysRoleMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class MapWithKeyTest extends BaseTest {

    @Test
    public void mapWithKeyTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);

            //最强mapWithKey 完全自己构建
            Map<Integer, SysRole> map = QueryChain.of(sysRoleMapper).mapWithKey(SysRole::getId);
            //根据where条件
            Map<String, SysRole> map1 = sysRoleMapper.mapWithKey(SysRole::getName, where -> {
                where.gt(SysRole::getCreateTime, LocalDate.parse("2023-01-01").atStartOfDay());
            });
            //根据多个id
            Map<Integer, SysRole> map2 = sysRoleMapper.mapWithKey(SysRole::getId, 1, 2, 3);
            //根据List<id>
            Map<Integer, SysRole> map3 = sysRoleMapper.mapWithKey(SysRole::getId, Arrays.asList(1, 2, 3));


            assertEquals(2, map.size());
            System.out.println(map);
            assertInstanceOf(SysRole.class, map.get(1));
            assertEquals(map.get(1).getName(), "测试");
            assertEquals(map.get(2).getName(), "运维");
        }
    }

    @Test
    public void mapWithKeyTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            this.configuration.setMapUnderscoreToCamelCase(true);
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);

            //最强mapWithKey 完全自己构建
            Map<Integer, SysRole> map = QueryChain.of(sysRoleMapper).mapWithKey(SysRole::getId);
            //根据where条件
            Map<String, SysRole> map1 = sysRoleMapper.mapWithKey(SysRole::getName, where -> {
                where.gt(SysRole::getCreateTime, LocalDate.parse("2023-01-01").atStartOfDay());
            });
            //根据多个id
            Map<Integer, SysRole> map2 = sysRoleMapper.mapWithKey(SysRole::getId, 1, 2, 3);
            //根据List<id>
            Map<Integer, SysRole> map3 = sysRoleMapper.mapWithKey(SysRole::getId, Arrays.asList(1, 2, 3));


            assertEquals(2, map.size());
            System.out.println(map);
            assertInstanceOf(SysRole.class, map.get(1));
            assertEquals(map.get(1).getName(), "测试");
            assertEquals(map.get(2).getName(), "运维");
        }
    }
}
