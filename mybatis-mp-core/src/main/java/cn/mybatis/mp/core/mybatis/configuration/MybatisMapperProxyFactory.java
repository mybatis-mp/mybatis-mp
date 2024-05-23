package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.util.GenericUtil;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;

public class MybatisMapperProxyFactory<T> extends MapperProxyFactory<T> {

    private final Class<?> entityType;

    private final TableInfo tableInfo;

    public MybatisMapperProxyFactory(Class<T> mapperInterface) {
        super(mapperInterface);
        this.entityType = GenericUtil.getGenericInterfaceClass(mapperInterface).get(0);
        this.tableInfo = Tables.get(this.entityType);
    }

    @Override
    public T newInstance(SqlSession sqlSession) {
        MapperProxy<T> mapperProxy = new MybatisMapperProxy(sqlSession, getMapperInterface(), getMethodCache(), entityType, tableInfo);
        return this.newInstance(mapperProxy);
    }
}
