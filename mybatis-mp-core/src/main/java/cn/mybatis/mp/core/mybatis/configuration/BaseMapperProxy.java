package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.db.annotations.Paging;
import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.reflection.ParamNameResolver;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class BaseMapperProxy<T> extends MapperProxy<T> {

    public final static String MAP_WITH_KEY_METHOD_NAME = "$mapWithKey";

    protected final SqlSession sqlSession;

    protected final Class<T> mapperInterface;

    public BaseMapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map methodCache) {
        super(sqlSession, mapperInterface, methodCache);
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            return super.invoke(proxy, method, args);
        }
        try {
            SqlSessionThreadLocalUtil.set(sqlSession);
            if (method.getName().equals(MAP_WITH_KEY_METHOD_NAME)) {
                return mapWithKey(method, args);
            } else if (method.isAnnotationPresent(Paging.class)) {
                return paging(method, args);
            }
            return super.invoke(proxy, method, args);
        } finally {
            SqlSessionThreadLocalUtil.clear();
        }

    }

    private <K, V> Map<K, V> mapWithKey(Method method, Object[] args) {
        MapKeySQLCmdQueryContext queryContext = (MapKeySQLCmdQueryContext) args[0];
        String statementId = mapperInterface.getName() + "." + method.getName();
        Map result = sqlSession.selectMap(statementId, queryContext, queryContext.getKey());
        return result;
    }

    private Pager paging(Method method, Object[] args) {
        ParamNameResolver paramNameResolver = new ParamNameResolver(this.sqlSession.getConfiguration(), method);
        Object params = paramNameResolver.getNamedParams(args);
        String statementId = mapperInterface.getName() + "." + method.getName();
        Pager pager = (Pager) args[0];
        Integer count = null;
        if (pager.isExecuteCount()) {
            count = sqlSession.selectOne(statementId + "-count", params);
            count = Objects.isNull(count) ? 0 : count;
        }

        List list;
        if (pager.isExecuteCount()) {
            if (count > 0) {
                list = sqlSession.selectList(statementId + "-list", params);
            } else {
                list = new ArrayList<>();
            }
        } else {
            list = sqlSession.selectList(statementId + "-list", params);
        }

        pager.setResults(list);
        pager.setTotal(count);
        return pager;
    }

}
