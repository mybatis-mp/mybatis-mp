package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.mybatis.mapper.DbRunner;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbRunnerTest extends BaseTest {

    @Test
    public void noParamTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            DbRunner dbRunner = session.getMapper(DbRunner.class);
            int cnt = dbRunner.execute("update t_sys_user set role_id=1 where id=1");
            assertEquals(cnt, 1);
        }
    }

    @Test
    public void oneParamTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            DbRunner dbRunner = session.getMapper(DbRunner.class);
            int cnt = dbRunner.execute("update t_sys_user set user_name=? where id=1", "xxx");
            assertEquals(cnt, 1);
        }
    }

    @Test
    public void multiParamTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            DbRunner dbRunner = session.getMapper(DbRunner.class);
            int cnt = dbRunner.execute("update t_sys_user set user_name=? where id=?", "xxx", 1);
            assertEquals(cnt, 1);
        }
    }

}
