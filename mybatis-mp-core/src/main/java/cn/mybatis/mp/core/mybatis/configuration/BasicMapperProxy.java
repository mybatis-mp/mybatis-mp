package cn.mybatis.mp.core.mybatis.configuration;


import org.apache.ibatis.session.SqlSession;

import java.util.Map;

public class BasicMapperProxy<T> extends BaseMapperProxy<T> {
    public BasicMapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map methodCache) {
        super(sqlSession, mapperInterface, methodCache);
    }
}
