package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.SubQuery;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

public class SubQueryTest extends BaseTest {

    @Test
    public void fromSubQueryTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            SubQuery subQuery = SubQuery.create("xx")
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2);

            QueryChain.of(sysUserMapper)
                    .select("*")
                    .from(subQuery)
                    .returnMap()
                    .list()
            ;
        }
    }

    @Test
    public void joinSubQueryTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);

            SubQuery subQuery = SubQuery.create("xx")
                    .select(SysUser::getId)
                    .from(SysUser.class)
                    .eq(SysUser::getId, 2);

            QueryChain.of(sysUserMapper)
                    .select("*")
                    .from(SysUser.class)
                    .join(SysUser.class, subQuery, on -> {
                        on.eq(SysUser::getId, subQuery.$outerField(SysUser::getId));
                    })
                    .returnMap()
                    .list()
            ;
        }
    }

}
