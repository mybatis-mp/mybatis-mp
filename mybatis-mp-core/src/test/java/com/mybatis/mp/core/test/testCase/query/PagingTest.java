package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PagingTest extends BaseTest {

    @Test
    public void pagingTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Pager<SysUser> pager = sysUserMapper.paging(Pager.of(2), where -> {
                where.in(SysUser::getId, 1, 2, 3);
            });

            assertEquals(pager.getTotal(), 3);
            assertEquals(pager.getTotalPage(), 2);

            assertEquals(pager.getResults().size(), 2);
            assertEquals(pager.getResults().get(0).getId(), 1);
            assertEquals(pager.getResults().get(1).getId(), 2);
        }
    }

    @Test
    public void pagingTest2() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            Pager<SysUser> pager = sysUserMapper.paging(Pager.of(2), WhereUtil.create().in(SysUser::getId, 1, 2, 3));

            assertEquals(pager.getTotal(), 3);
            assertEquals(pager.getTotalPage(), 2);

            assertEquals(pager.getResults().size(), 2);
            assertEquals(pager.getResults().get(0).getId(), 1);
            assertEquals(pager.getResults().get(1).getId(), 2);
        }
    }
}
