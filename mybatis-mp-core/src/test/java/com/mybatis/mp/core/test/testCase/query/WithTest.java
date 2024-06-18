package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.executor.WithQuery;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.tookit.SQLPrinter;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
        }
    }

    @Test
    public void withQueryAs() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            WithQuery withQuery = WithQuery.create("sub")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .orderBy(SysRole::getId)
                    .limit(111)
                    .eq(SysRole::getId, 1);

            Table aaa = withQuery.asTable("aaa");
            Table bbb = withQuery.asTable("bbb");

            QueryChain<SysUser> queryChain = QueryChain.of(sysUserMapper);
            queryChain
                    .with(withQuery)
                    .select(aaa, SysRole::getId, c -> c.as("xx"))
                    .selectWithFun(bbb, "id", c -> c.plus(1).as("xx2"))
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .from(aaa, bbb)
                    .eq(SysUser::getRole_id, aaa.$(SysUser::getId))
                    .eq(SysUser::getRole_id, bbb.$(SysUser::getId))
                    .orderBy(aaa, SysRole::getId)
                    .orderBy(bbb, SysRole::getId);

            Pager<SysUser> page = queryChain.paging(Pager.of(100));
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


            check("检测with  join", "WITH sub AS ( SELECT  wt.id ,  wt.name ,  wt.create_time FROM sys_role wt WHERE  wt.id = 1 ORDER BY  wt.id ASC LIMIT 111 OFFSET 0) SELECT  sub.id AS xx , ( sub.id + 1) AS xx2 ,  t.id ,  t.user_name ,  t.password ,  t.role_id ,  t.create_time FROM t_sys_user t  INNER JOIN  sub ON  t.role_id =  sub.id ORDER BY  sub.id ASC LIMIT 100 OFFSET 0", queryChain);

        }
    }

    @Test
    public void withQueryMuti() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            WithQuery withQuery1 = WithQuery.create("sub")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .orderBy(SysRole::getId)
                    .limit(111)
                    .eq(SysRole::getId, 1);

            WithQuery withQuery2 = WithQuery.create("sub2")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .orderBy(SysRole::getId)
                    .limit(111)
                    .eq(SysRole::getId, 1);


            QueryChain<SysUser> queryChain = QueryChain.of(sysUserMapper);
            queryChain
                    .with(withQuery1, withQuery2)
                    .select(withQuery1, SysRole::getId, c -> c.as("xx"))
                    .selectWithFun(withQuery2, "id", c -> c.plus(1).as("xx2"))
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .from(withQuery1, withQuery2)
                    .eq(SysUser::getRole_id, withQuery1.$("id"))
                    .eq(SysUser::getRole_id, withQuery2.$("id"))
                    .orderBy(withQuery1, SysRole::getId)
                    .orderBy(withQuery2, SysRole::getId);

            Pager<SysUser> page = queryChain.paging(Pager.of(100));
            System.out.println(SQLPrinter.sql(queryChain));
            assertEquals(2, page.getResults().size(), "withQuery");
        }
    }


    @Test
    public void withRecursiveQuery() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            WithQuery withQuery = WithQuery.create("sub")
                    .recursive("n", "n2")
                    .select("1,1")
                    .dbAdapt((query, selector) -> {
                        selector.when(DbType.ORACLE, () -> {
                            query.from(new Table("dual"));
                        }).when(DbType.DB2, () -> {
                            query.from(new Table("SYSIBM.SYSDUMMY1"));
                        }).otherwise();
                    });

            withQuery.unionAll(Query.create()
                    .select("n+1,n2+1")
                    .from(withQuery)
                    .lt(Methods.column("n"), 2)
                    .lt(Methods.column("n2"), 3)
            );

            List<Map<String, Object>> mapList = QueryChain.of(sysUserMapper)
                    .with(withQuery)
                    .selectAll(withQuery)
                    .from(withQuery)
                    .returnMap()
                    .list();

            System.out.println(mapList);
        }
    }


    @Test
    public void withRecursiveQuery2() {

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            WithQuery withQuery = WithQuery.create("dept_with")
                    .recursive()
                    .select(SysUser::getId, SysUser::getRole_id)
                    .from(SysUser.class)
                    .eq(SysUser::getRole_id, 100);

            withQuery.unionAll(Query.create()
                    .select(SysUser::getId, SysUser::getRole_id)
                    .from(SysUser.class)
            );

            List<Map<String, Object>> mapList = QueryChain.of(sysUserMapper)
                    .with(withQuery)
                    .selectAll()
                    .from(withQuery)
                    .returnMap()
                    .list();

            System.out.println(mapList);
        }
    }
}
