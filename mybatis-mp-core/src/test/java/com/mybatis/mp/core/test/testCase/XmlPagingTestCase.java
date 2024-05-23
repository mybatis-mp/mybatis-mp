package com.mybatis.mp.core.test.testCase;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.mapper.SysRoleMapper;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XmlPagingTestCase extends BaseTest {

    @Test
    public void xmlPaging() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            Pager<SysRole> pager = sysRoleMapper.xmlPaging(Pager.of(1), 1, 1);
            assertEquals(1, pager.getTotal());
            assertEquals(1, pager.getResults().get(0).getId());
            assertNotNull(pager.getResults().get(0).getCreateTime());


            pager = sysRoleMapper.xmlPaging(Pager.of(2), 1, 2);
            assertEquals(2, pager.getTotal());
            assertEquals(2, pager.getResults().get(1).getId());
            assertNotNull(pager.getResults().get(1).getCreateTime());
            System.out.println(pager);
        }
    }

    @Test
    public void xmlNoParamsPaging() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            Pager<SysRole> pager = sysRoleMapper.xmlPaging(Pager.of(1), 1, 1);

            pager = sysRoleMapper.xmlPaging2(Pager.of(2));
            assertEquals(2, pager.getTotal());
            assertEquals(2, pager.getResults().get(1).getId());
            System.out.println(pager);
            assertNull(pager.getResults().get(1).getCreateTime());
        }
    }

    @Test
    public void xmlDynamicPaging() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            Pager<SysRole> pager;

            //pager = sysRoleMapper.xmlPaging(Pager.of(1), 1, 1);

            pager = sysRoleMapper.xmlDynamicPaging(Pager.of(2), 1, 2, 1);
            assertEquals(1, pager.getTotal());
            assertEquals(1, pager.getResults().get(0).getId());
            assertNotNull(pager.getResults().get(0).getCreateTime());
        }
    }

    @Test
    public void annotationPaging() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysRoleMapper sysRoleMapper = session.getMapper(SysRoleMapper.class);
            Pager<SysRole> pager = sysRoleMapper.annotationPaging(Pager.of(2), 1, 2);
            assertEquals(2, pager.getTotal());
            assertEquals(2, pager.getResults().get(1).getId());
            assertNotNull(pager.getResults().get(0).getCreateTime());
            System.out.println(pager);
        }
    }

}
