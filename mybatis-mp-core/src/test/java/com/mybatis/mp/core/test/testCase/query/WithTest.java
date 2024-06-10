package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.WithQuery;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import db.sql.api.impl.tookit.SQLPrinter;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WithTest extends BaseTest {

    @Test
    public void withQuery() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            WithQuery withQuery = WithQuery.create("sub")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .orderBy(SysRole::getId)
                    .limit(111)
                    .eq(SysRole::getId, 1);

            QueryChain<SysUser> queryChain = QueryChain.of(sysUserMapper);
            queryChain
                    .with(withQuery)
                    .select(withQuery, SysRole::getId, c -> c.as("xx"))
                    .selectWithFun(withQuery, "id", c -> c.plus(1).as("xx2"))
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .from(withQuery)
                    .eq(SysUser::getRole_id, withQuery.$(withQuery, SysRole::getId))
                    .orderBy(withQuery, SysRole::getId);

            Pager<SysUser> page = queryChain.paging(Pager.of(100));
            System.out.println(SQLPrinter.sql(queryChain));
            assertEquals(2, page.getResults().size(), "withQuery");

            page = queryChain.paging(Pager.of(100).setOptimize(false));
            System.out.println(SQLPrinter.sql(queryChain));
            assertEquals(2, page.getResults().size(), "withQuery");
        }
    }

    @Test
    public void withQueryJoin() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            WithQuery withQuery = WithQuery.create("sub")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .orderBy(SysRole::getId)
                    .limit(111)
                    .eq(SysRole::getId, 1);

            QueryChain<SysUser> queryChain = QueryChain.of(sysUserMapper);
            queryChain
                    .with(withQuery)
                    .select(withQuery, SysRole::getId, c -> c.as("xx"))
                    .selectWithFun(withQuery, "id", c -> c.plus(1).as("xx2"))
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .join(SysUser.class, withQuery, on -> on.eq(SysUser::getRole_id, withQuery.$(withQuery, SysRole::getId)))
                    .orderBy(withQuery, SysRole::getId);

            Pager<SysUser> page = queryChain.paging(Pager.of(100));
            System.out.println(SQLPrinter.sql(queryChain));
            assertEquals(2, page.getResults().size(), "withQuery");

            page = queryChain.paging(Pager.of(100).setOptimize(false));
            System.out.println(SQLPrinter.sql(queryChain));
            assertEquals(2, page.getResults().size(), "withQuery");

            check("检测with  join", "WITH sub AS ( SELECT  wt.id ,  wt.name ,  wt.create_time FROM sys_role wt WHERE  wt.id = 1 ORDER BY  wt.id ASC LIMIT 111 OFFSET 0) SELECT  sub.id AS xx , ( sub.id + 1) AS xx2 ,  t.id ,  t.user_name ,  t.password ,  t.role_id ,  t.create_time FROM t_sys_user t  INNER JOIN  sub ON  t.role_id =  sub.id ORDER BY  sub.id ASC LIMIT 100 OFFSET 0", queryChain);

        }
    }
}
