package cn.mybatis.mp.core.mybatis.configuration;

import org.apache.ibatis.session.SqlSession;

public final class SqlSessionThreadLocalUtil {

    private final static ThreadLocal<SqlSession> localSqlSession = new ThreadLocal<>();

    public static void set(SqlSession sqlSession) {
        localSqlSession.set(sqlSession);
    }

    public static SqlSession get() {
        return localSqlSession.get();
    }

    public static void clear() {
        localSqlSession.remove();
    }
}
