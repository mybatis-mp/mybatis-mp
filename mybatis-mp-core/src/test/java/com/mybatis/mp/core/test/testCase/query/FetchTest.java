package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysRoleMapper;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import com.mybatis.mp.core.test.vo.*;
import db.sql.api.DbType;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FetchTest extends BaseTest {

    @Test
    public void onRowEvent() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            LongAdder longAdder = new LongAdder();
            QueryChain.of(sysUserMapper)
                    .returnType(SysUser.class, sysUser -> {
                        System.out.println(sysUser);
                        if (sysUser instanceof SysUser) {
                            longAdder.increment();
                        } else {
                            throw new RuntimeException("returnTypeEach item not instanceof SysUser");
                        }
                    })
                    .mapWithKey(SysUser::getRole_id);
            System.out.println(longAdder.sum());
            assertTrue(longAdder.sum() > 0);
        }
    }

    @Test
    public void onRowEventVO() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            LongAdder longAdder = new LongAdder();
            QueryChain.of(sysUserMapper)
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .returnType(SysUserVo.class, sysUserVo -> {
                        System.out.println(sysUserVo);
                        if (sysUserVo instanceof SysUserVo) {
                            longAdder.increment();
                        } else {
                            throw new RuntimeException("returnTypeEach item not instanceof SysUser");
                        }
                    })
                    .mapWithKey(SysUserVo::getId);
            System.out.println(longAdder.sum());
            assertTrue(longAdder.sum() > 0);
        }
    }

    @Test
    public void fetchWithNoRootField() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<FetchSysUserVo> list = QueryChain.of(sysUserMapper)
                    .select(SysUser::getRole_id)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2)
                    .returnType(FetchSysUserVo.class)
                    .list();
            System.out.println(list);
            assertEquals("测试", list.get(0).getSysRole().getName());
        }
    }


    @Test
    public void fetchByAllNested() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            List<FetchSysRoleVoWhenHasNested> list = QueryChain.of(sysUserMapper)
                    .select(FetchSysRoleVoWhenHasNested.class)
                    .from(SysUser.class)
                    .returnType(FetchSysRoleVoWhenHasNested.class)
                    .list();
            System.out.println(list);
            assertEquals("测试", list.get(1).getSysRole().getName());
            assertEquals("测试", list.get(2).getSysRole().getName());

            assertEquals("admin", list.get(0).getSysUser().getUserName());
            assertEquals("test1", list.get(1).getSysUser().getUserName());
            assertEquals("test2", list.get(2).getSysUser().getUserName());
        }
    }

    @Test
    public void fetchFilterByAllNested() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            List<FetchSysRoleVoWhenHasNested> list = QueryChain.of(sysUserMapper)
                    .select(FetchSysRoleVoWhenHasNested.class)
                    .from(SysUser.class)
                    .fetchFilter(FetchSysRoleVoWhenHasNested::getSysRole, where -> {
                        where.eq(SysRole::getId, 0);
                    })
                    .returnType(FetchSysRoleVoWhenHasNested.class)
                    .list();
            System.out.println(list);
            list.forEach(item -> assertEquals(null, item.getSysRole()));
        }
    }

    @Test
    public void fetchByPropertyTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<FetchSysUserVo> list = QueryChain.of(sysUserMapper)
                    .select(FetchSysUserVo.class)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2)
                    .fetchFilter(FetchSysUserVo::getSysRole, where -> where.eq(SysRole::getId, 1))
                    .fetchFilter(SysRoleVo::getSysRole, where -> where.eq(SysRole::getId, 1))
                    .returnType(FetchSysUserVo.class)
                    .list();
            System.out.println(list);
            assertEquals("测试", list.get(0).getSysRole().getName());
            assertEquals(list.get(0).getSysRole().getId(), list.get(0).getSysRole().getSysRole().getId());
        }
    }

    @Test
    public void fetchByPropertyTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<FetchSysUserVo> list = QueryChain.of(sysUserMapper)
                    .select(FetchSysUserVo.class)
                    .select(SysUser::getRole_id)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2)
                    .returnType(FetchSysUserVo.class)
                    .list();
            System.out.println(list);
            assertEquals("测试", list.get(0).getSysRole().getName());
        }
    }


    @Test
    public void fetchMutiByPropertyTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<MutiFetchSysUserVo> list = QueryChain.of(sysUserMapper)
                    .select(MutiFetchSysUserVo.class)
                    .from(SysUser.class)
                    .returnType(MutiFetchSysUserVo.class)
                    .list();
            System.out.println(list);
            assertEquals(Integer.valueOf(1), list.get(1).getSysRole().get(0).getId());
            assertEquals("测试", list.get(1).getSysRole().get(0).getName());
            assertEquals(Integer.valueOf(1), list.get(1).getSysRole().size());
        }
    }


    @Test
    public void fetchMutiByPropertyTest2() {
        if (TestDataSource.DB_TYPE == DbType.SQL_SERVER || TestDataSource.DB_TYPE == DbType.DB2) {
            return;
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            SysRole sysRole = new SysRole();
            sysRole.setId(4);
            sysRole.setName("cesh2");
            sysRole.setCreateTime(LocalDateTime.now());
            sysRoleMapper.save(sysRole);

            SysUser sysUser = new SysUser();
            sysUser.setId(4);
            sysUser.setRole_id(4);
            sysUser.setUserName("xx");
            sysUser.setCreate_time(LocalDateTime.now());
            sysUserMapper.save(sysUser);

            QueryChain<FetchSysRoleVo> queryChain = QueryChain.of(sysRoleMapper)
                    .select(FetchSysRoleVo.class)
                    .from(SysRole.class)
                    .returnType(FetchSysRoleVo.class);


            FetchSysRoleVo roleVo = new FetchSysRoleVo();
            roleVo.setId(1);
            roleVo.setName("测试");

            FetchSysUserForRoleVo userVo = new FetchSysUserForRoleVo();
            userVo.setId(2);
            userVo.setRole_id(1);
            userVo.setUserName("test1");
            userVo.setPassword("123456");
            userVo.setCreate_time(LocalDateTime.parse("2023-10-11T15:16:17"));

            FetchSysUserForRoleVo userVo2 = new FetchSysUserForRoleVo();
            userVo2.setId(3);
            userVo2.setRole_id(1);
            userVo2.setUserName("test2");
            userVo2.setCreate_time(LocalDateTime.parse("2023-10-12T15:16:17"));

            roleVo.setSysRole(Arrays.asList(userVo, userVo2));
            roleVo.setSysRoleNames(Arrays.asList("test1", "test2"));

            Map<Integer, FetchSysRoleVo> voMap = queryChain.mapWithKey(FetchSysRoleVo::getId);

            System.out.println(voMap);
            assertEquals(roleVo, voMap.get(1));
            assertEquals("xx", voMap.get(4).getSysRoleNames().get(0));
            assertEquals("xx", voMap.get(4).getSysRole().get(0).getUserName());

            List<FetchSysRoleVo> list = queryChain
                    .list();
            System.out.println(list);
            assertEquals(roleVo, list.get(0));


//            assertEquals(Integer.valueOf(1), list.get(1).getSysRole().get(0).getId());
//            assertEquals("测试", list.get(1).getSysRole().get(0).getName());
//            assertEquals(Integer.valueOf(1), list.get(1).getSysRole().size());
        }
    }
}
