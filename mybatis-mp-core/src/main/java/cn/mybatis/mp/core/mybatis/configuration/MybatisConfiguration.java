package cn.mybatis.mp.core.mybatis.configuration;


import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdContext;
import cn.mybatis.mp.core.mybatis.resultset.MybatisDefaultResultSetHandler;
import cn.mybatis.mp.core.mybatis.typeHandler.EnumTypeHandler;
import cn.mybatis.mp.core.mybatis.typeHandler.MybatisTypeHandlerUtil;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.db.annotations.Table;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.executor.*;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;


public class MybatisConfiguration extends Configuration {

    public MybatisConfiguration() {
        super();
        this.init();
    }

    public MybatisConfiguration(Environment environment) {
        super(environment);
        this.init();
    }

    private void init() {
        this.setDefaultScriptingLanguage(MybatisLanguageDriver.class);
        this.setDefaultEnumTypeHandler(EnumTypeHandler.class);
    }

    public void printBanner() {
        try (BufferedReader reader = new BufferedReader(Resources.getResourceAsReader("mybatis-mp.banner"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameterObject, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) {
        mappedStatement = DynamicsMappedStatement.wrapMappedStatement(mappedStatement, parameterObject);
        return super.newStatementHandler(executor, mappedStatement, parameterObject, rowBounds, resultHandler, boundSql);
    }

    @Override
    public ParameterHandler newParameterHandler(MappedStatement ms, Object parameterObject, BoundSql boundSql) {
        if (parameterObject instanceof SQLCmdContext && !ms.getId().endsWith(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
            return (ParameterHandler) interceptorChain.pluginAll(new PreparedParameterHandler(this, (SQLCmdContext) parameterObject));
        }
        return super.newParameterHandler(ms, parameterObject, boundSql);
    }

    @Override
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, RowBounds rowBounds, ParameterHandler parameterHandler, ResultHandler resultHandler, BoundSql boundSql) {
        ResultSetHandler resultSetHandler = new MybatisDefaultResultSetHandler(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        return (ResultSetHandler) interceptorChain.pluginAll(resultSetHandler);
    }

    @Override
    public void addMappedStatement(MappedStatement ms) {
        super.addMappedStatement(MappedStatementUtil.wrap(ms));
    }


    private <T> void addBasicMapper(Class<T> type) {
        super.addMapper(type);
        //替换成自己的   MapperProxy 工厂
        if (this.mapperRegistry.hasMapper(type)) {
            MetaObject msMetaObject = this.newMetaObject(this.mapperRegistry);
            Map<Class<?>, MapperProxyFactory<?>> knownMappers = (Map<Class<?>, MapperProxyFactory<?>>) msMetaObject.getValue("knownMappers");
            knownMappers.put(type, new BasicMapperProxyFactory(type));
        }
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        //添加基础 BasicMapper
        if (!this.hasMapper(BasicMapper.class)) {
            this.addBasicMapper(BasicMapper.class);
        }

        if (MybatisMapper.class.isAssignableFrom(type)) {
            Optional<Class<?>> entityOptional = GenericUtil.getGenericInterfaceClass(type).stream().filter(item -> item.isAnnotationPresent(Table.class)).findFirst();
            if (!entityOptional.isPresent()) {
                throw new RuntimeException(type + " did not add a generic");
            }
            ResultMapUtils.getResultMap(this, entityOptional.get());
        }

        if (BasicMapper.class.isAssignableFrom(type)) {
            this.addBasicMapper(type);
            return;
        }

        super.addMapper(type);

        if (MybatisMapper.class.isAssignableFrom(type)) {
            //替换成自己的   MapperProxy 工厂
            if (this.mapperRegistry.hasMapper(type)) {
                MetaObject msMetaObject = this.newMetaObject(this.mapperRegistry);
                Map<Class<?>, MapperProxyFactory<?>> knownMappers = (Map<Class<?>, MapperProxyFactory<?>>) msMetaObject.getValue("knownMappers");
                knownMappers.put(type, new MybatisMapperProxyFactory(type));
            }
        }
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return super.getMapper(type, sqlSession);
    }

    public ResultMapping buildResultMapping(boolean id, Field property, String columnName, JdbcType jdbcType, Class<? extends TypeHandler<?>> typeHandlerClass) {
        ResultMapping.Builder resultMappingBuilder = new ResultMapping.Builder(this, property.getName())
                .column(columnName)
                .javaType(property.getType())
                .jdbcType(jdbcType)
                .typeHandler(MybatisTypeHandlerUtil.getTypeHandler(this, property, typeHandlerClass, jdbcType));
        if (id) {
            resultMappingBuilder.flags(Collections.singletonList(ResultFlag.ID));
        }
        return resultMappingBuilder.build();
    }

    @Override
    public Executor newExecutor(Transaction transaction, ExecutorType executorType) {
        executorType = executorType == null ? this.defaultExecutorType : executorType;
        Executor executor;
        if (ExecutorType.BATCH == executorType) {
            executor = new BatchExecutor(this, transaction);
        } else if (ExecutorType.REUSE == executorType) {
            executor = new ReuseExecutor(this, transaction);
        } else {
            executor = new SimpleExecutor(this, transaction);
        }
        executor = new MybatisExecutor(executor);
        if (this.cacheEnabled) {
            executor = new CachingExecutor(executor);
        }
        return (Executor) this.interceptorChain.pluginAll(executor);
    }
}



