package cn.mybatis.mp.core.mybatis.configuration;

import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;

public class BasicMapperProxyFactory<T> extends MapperProxyFactory<T> {

    public BasicMapperProxyFactory(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    @Override
    public T newInstance(SqlSession sqlSession) {
        BasicMapperProxy<T> mapperProxy = new BasicMapperProxy(sqlSession, getMapperInterface(), getMethodCache());
        return this.newInstance(mapperProxy);
    }
}
