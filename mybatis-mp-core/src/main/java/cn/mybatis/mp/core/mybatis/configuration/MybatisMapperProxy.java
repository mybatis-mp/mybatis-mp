package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import org.apache.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class MybatisMapperProxy<T> extends BaseMapperProxy<T> {

    public final static String ENTITY_TYPE_METHOD_NAME = "getEntityType";
    public final static String MAPPER_TYPE_METHOD_NAME = "getMapperType";
    public final static String TABLE_INFO_METHOD_NAME = "getTableInfo";
    public final static String GET_BASIC_MAPPER_METHOD_NAME = "getBasicMapper";

    private final SqlSession sqlSession;

    private final Class<T> mapperInterface;
    private final Class<?> entityType;
    private final TableInfo tableInfo;
    private volatile BasicMapper basicMapper;

    public MybatisMapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map methodCache, Class<?> entityType, TableInfo tableInfo) {
        super(sqlSession, mapperInterface, methodCache);
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.entityType = entityType;
        this.tableInfo = tableInfo;
    }

    private BasicMapper getBasicMapper() {
        if (Objects.isNull(basicMapper)) {
            basicMapper = sqlSession.getMapper(BasicMapper.class);
        }
        return basicMapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.isDefault()) {
            return super.invoke(proxy, method, args);
        }
        try {
            SqlSessionThreadLocalUtil.set(sqlSession);
            if (method.getName().equals(ENTITY_TYPE_METHOD_NAME)) {
                return this.entityType;
            } else if (method.getName().equals(MAPPER_TYPE_METHOD_NAME)) {
                return this.mapperInterface;
            } else if (method.getName().equals(TABLE_INFO_METHOD_NAME)) {
                return this.tableInfo;
            } else if (method.getName().equals(GET_BASIC_MAPPER_METHOD_NAME)) {
                return getBasicMapper();
            }
            return super.invoke(proxy, method, args);
        } finally {
            SqlSessionThreadLocalUtil.clear();
        }
    }
}
