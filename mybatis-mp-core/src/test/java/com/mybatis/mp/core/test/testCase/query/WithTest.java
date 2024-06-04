package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.SubQuery;
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

            SubQuery subQuery = SubQuery.create("sub")
                    .select(SysRole.class)
                    .from(SysRole.class)
                    .orderBy(SysRole::getId)
                    .limit(111)
                    .eq(SysRole::getId, 1);
            QueryChain<SysUser> queryChain = QueryChain.of(sysUserMapper);
            queryChain
                    .with(subQuery)
                    .select(subQuery, SysRole::getId, c -> c.as("xx"))
                    .selectWithFun(subQuery, "id", c -> c.plus(1).as("xx2"))
                    .select(SysUser.class)
                    .from(SysUser.class)
                    .from(subQuery)
                    .eq(SysUser::getRole_id, subQuery.$().field(subQuery, SysRole::getId))
                    .orderBy(subQuery, SysRole::getId);

            Pager<SysUser> page = queryChain.paging(Pager.of(100));
            System.out.println(SQLPrinter.sql(queryChain));
            assertEquals(2, page.getResults().size(), "withQuery");

            page = queryChain.paging(Pager.of(100).setOptimize(false));
            System.out.println(SQLPrinter.sql(queryChain));
            assertEquals(2, page.getResults().size(), "withQuery");
        }
    }
}
