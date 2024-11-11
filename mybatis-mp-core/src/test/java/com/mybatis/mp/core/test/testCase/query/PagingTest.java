package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import com.mybatis.mp.core.test.DO.MultiPk;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.MultiPkMapper;
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

    @Test
    public void pagingTest3() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper multiPkMapper = session.getMapper(MultiPkMapper.class);
            MultiPk multiPk = new MultiPk();
            multiPk.setId1(1);
            multiPk.setId2(2);
            multiPk.setName("xxx");
            multiPkMapper.save(multiPk);
            Pager<MultiPk> pager = multiPkMapper.paging(Pager.of(2), where -> {

            });
            System.out.println(pager.getResults());

            assertEquals(pager.getTotal(), 1);
            assertEquals(pager.getTotalPage(), 1);

            assertEquals(pager.getResults().size(), 1);
            assertEquals(pager.getResults().get(0).getId1(), 1);
            assertEquals(pager.getResults().get(0).getId2(), 2);
        }
    }
}
