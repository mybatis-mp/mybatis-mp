package cn.mybatis.mp.core.mybatis.resultset;

import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.core.mybatis.configuration.FetchObject;
import cn.mybatis.mp.core.mybatis.configuration.SqlSessionThreadLocalUtil;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdQueryContext;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.util.CreatedEventUtil;
import cn.mybatis.mp.core.util.PutEnumValueUtil;
import cn.mybatis.mp.core.util.PutValueUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.db.annotations.CreatedEvent;
import cn.mybatis.mp.db.annotations.ResultEntity;
import db.sql.api.impl.cmd.basic.Column;
import db.sql.api.impl.cmd.basic.OrderByDirection;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetWrapper;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.util.MapUtil;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MybatisDefaultResultSetHandler extends DefaultResultSetHandler {

    private final Map<FetchInfo, Map<Object, List<Object>>> singleFetchCache = new HashMap<>();

    private volatile BasicMapper basicMapper;

    private Map<FetchInfo, List<FetchObject>> needFetchValuesMap;
    //Fetch 信息
    private Map<Class, List<FetchInfo>> fetchInfosMap;

    private Map<String, Consumer<Where>> fetchFilters;

    private Consumer onRowEvent;

    private Class<?> returnType;

    private Map<Class, List<PutEnumValueInfo>> putEnumValueInfoMap;

    private Map<Class, List<PutValueInfo>> putValueInfoMap;

    private List<CreatedEvent> createdEventList;

    public MybatisDefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds) {
        super(executor, mappedStatement, parameterHandler, resultHandler, boundSql, rowBounds);
        if (mappedStatement.getResultMaps().size() == 1) {
            Class<?> returnType = mappedStatement.getResultMaps().get(0).getType();
            if (returnType.isAnnotationPresent(ResultEntity.class)) {
                ResultInfo resultInfo = ResultInfos.get(returnType);
                this.fetchInfosMap = resultInfo.getFetchInfoMap();
                if (Objects.nonNull(this.fetchInfosMap) && !this.fetchInfosMap.isEmpty()) {
                    this.needFetchValuesMap = new HashMap<>();
                }

                if (boundSql.getParameterObject() instanceof SQLCmdQueryContext) {
                    BaseQuery<?, ?> baseQuery = ((SQLCmdQueryContext) boundSql.getParameterObject()).getExecution();
                    this.fetchFilters = baseQuery.getFetchFilters();
                    this.putEnumValueInfoMap = resultInfo.getPutEnumValueInfoMap();
                    this.putValueInfoMap = resultInfo.getPutValueInfoMap();
                    this.createdEventList = resultInfo.getCreatedEventList();
                }
            }

            if (boundSql.getParameterObject() instanceof SQLCmdQueryContext) {
                BaseQuery<?, ?> baseQuery = ((SQLCmdQueryContext) boundSql.getParameterObject()).getExecution();
                this.onRowEvent = baseQuery.getOnRowEvent();
                this.returnType = baseQuery.getReturnType();
            }
        }
    }

    private void onRowEvent(Object rowValue) {
        if (Objects.isNull(onRowEvent)) {
            return;
        }
        if (Objects.isNull(rowValue)) {
            return;
        }
        if (!rowValue.getClass().isAssignableFrom(returnType)) {
            return;
        }
        onRowEvent.accept(rowValue);
    }

    private void putEnumValue(Object rowValue, ResultSet resultSet) {
        if (Objects.isNull(putEnumValueInfoMap)) {
            return;
        }
        if (Objects.isNull(rowValue)) {
            return;
        }

        List<PutEnumValueInfo> putEnumValueInfos = putEnumValueInfoMap.get(rowValue.getClass());
        if (Objects.isNull(putEnumValueInfos) || putEnumValueInfos.isEmpty()) {
            return;
        }
        putEnumValueInfos.stream().forEach(item -> {
            Object codeValue;
            try {
                if (Objects.nonNull(item.getValueTypeHandler())) {
                    codeValue = item.getValueTypeHandler().getResult(resultSet, item.getValueColumn());
                } else {
                    codeValue = resultSet.getObject(item.getValueColumn());
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Object targetValue = PutEnumValueUtil.getEnumValue(codeValue, item);
            if (Objects.isNull(targetValue)) {
                return;
            }
            try {
                item.getWriteFieldInvoker().invoke(rowValue,
                        new Object[]{targetValue});
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }


    private void putValue(Object rowValue, ResultSet resultSet) {
        if (Objects.isNull(putValueInfoMap)) {
            return;
        }
        if (Objects.isNull(rowValue)) {
            return;
        }

        List<PutValueInfo> putValueInfos = putValueInfoMap.get(rowValue.getClass());
        if (Objects.isNull(putValueInfos) || putValueInfos.isEmpty()) {
            return;
        }
        putValueInfos.stream().forEach(item -> {
            Object[] values = new Object[item.getValuesColumn().length];
            for (int i = 0; i < item.getValuesColumn().length; i++) {
                try {
                    if (Objects.nonNull(item.getValuesTypeHandler()[i])) {
                        values[i] = item.getValuesTypeHandler()[i].getResult(resultSet, item.getValuesColumn()[i]);
                    } else {
                        values[i] = resultSet.getObject(item.getValuesColumn()[i]);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }

            Object targetValue = PutValueUtil.getPutValue(values, item);

            if (Objects.isNull(targetValue)) {
                return;
            }

            try {
                item.getWriteFieldInvoker().invoke(rowValue,
                        new Object[]{targetValue});
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });

    }

    private void putValues(Object rowValue) {
        if (Objects.isNull(this.createdEventList)) {
            return;
        }
        if (Objects.isNull(rowValue)) {
            return;
        }
        this.createdEventList.stream().forEach(item -> {
            CreatedEventUtil.onCreated(rowValue, item);
        });

    }

    @Override
    protected Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, String columnPrefix) throws SQLException {
        Object rowValue = super.getRowValue(rsw, resultMap, columnPrefix);
        rowValue = this.loadFetchValue(resultMap.getType(), rowValue, rsw.getResultSet());
        this.putEnumValue(rowValue, rsw.getResultSet());
        this.putValue(rowValue, rsw.getResultSet());
        this.onRowEvent(rowValue);
        this.putValues(rowValue);
        return rowValue;
    }

    @Override
    protected Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap, CacheKey combinedKey, String columnPrefix, Object partialObject) throws SQLException {
        Object rowValue = super.getRowValue(rsw, resultMap, combinedKey, columnPrefix, partialObject);
        rowValue = this.loadFetchValue(resultMap.getType(), rowValue, rsw.getResultSet());
        this.putEnumValue(rowValue, rsw.getResultSet());
        this.putValue(rowValue, rsw.getResultSet());
        this.onRowEvent(rowValue);
        this.putValues(rowValue);
        return rowValue;
    }

    @Override
    public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler, RowBounds rowBounds, ResultMapping parentMapping) throws SQLException {
        super.handleRowValues(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        this.handleFetch();
    }

    public Object loadFetchValue(Class<?> resultType, Object rowValue, ResultSet resultSet) throws SQLException {
        if (Objects.isNull(fetchInfosMap) || fetchInfosMap.isEmpty()) {
            return rowValue;
        }

        List<FetchInfo> fetchInfos = fetchInfosMap.get(resultType);
        if (Objects.isNull(fetchInfos) || fetchInfos.isEmpty()) {
            return rowValue;
        }

        for (FetchInfo fetchInfo : fetchInfos) {
            Object onValue;
            try {
                if (Objects.nonNull(fetchInfo.getValueTypeHandler())) {
                    onValue = fetchInfo.getValueTypeHandler().getResult(resultSet, fetchInfo.getValueColumn());
                } else {
                    onValue = resultSet.getObject(fetchInfo.getValueColumn());
                    if (!(onValue instanceof Number)) {
                        onValue = resultSet.getString(fetchInfo.getValueColumn());
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (Objects.isNull(onValue)) {
                continue;
            }

            if (Objects.isNull(rowValue) && resultSet.getRow() > 0) {
                rowValue = configuration.getObjectFactory().create(resultType);
            }

            if (Objects.isNull(fetchInfo.getEqGetFieldInvoker()) || fetchInfo.getFetch().limit() > 0 || (Objects.nonNull(fetchInfo.getTargetSelectColumn()) && fetchInfo.getTargetSelectColumn().contains("("))) {
                this.singleConditionFetch(rowValue, fetchInfo, onValue);
            } else {
                MapUtil.computeIfAbsent(needFetchValuesMap, fetchInfo, key -> new ArrayList<>()).add(new FetchObject(onValue, onValue.toString(), rowValue));
            }
        }

        return rowValue;
    }

    public void handleFetch() {
        if (Objects.isNull(this.needFetchValuesMap) || this.needFetchValuesMap.isEmpty()) {
            return;
        }
        needFetchValuesMap.forEach(((fetchInfo, fetchObjects) -> {
            List<Object> list = this.fetchData(fetchInfo, fetchObjects.stream().map(item -> item.getSourceKey()).distinct().collect(Collectors.toList()));
            this.fillFetchData(fetchInfo, fetchObjects, list);
        }));
    }

    private List<Object> fetchData(FetchInfo fetchInfo, Query<Object> query, List<Serializable> queryValueList) {
        if (Objects.isNull(basicMapper)) {
            basicMapper = this.configuration.getMapper(BasicMapper.class, SqlSessionThreadLocalUtil.get());
        }
        if (queryValueList.isEmpty()) {
            return Collections.emptyList();
        }
        if (Objects.nonNull(query.$where().getConditionChain())) {
            query.$where().getConditionChain().getConditionBlocks().clear();
        }

        query.from(fetchInfo.getFetch().target(), table -> {
            if (queryValueList.size() == 1) {
                if (fetchInfo.getFetch().limit() > 0) {
                    query.limit(fetchInfo.getFetch().limit());
                }
                query.eq(table.$(fetchInfo.getTargetMatchColumn()), queryValueList.get(0));
            } else {
                query.in(table.$(fetchInfo.getTargetMatchColumn()), queryValueList);
            }
        });

        String fetchKey = fetchInfo.getField().getDeclaringClass().getName() + "." + fetchInfo.getField().getName();
        boolean hasFetchFilter = !Objects.isNull(fetchFilters) && fetchFilters.containsKey(fetchKey);
        query.setFetchFilters(fetchFilters);
        if (hasFetchFilter) {
            fetchFilters.get(fetchKey).accept(query.$where());
        }
        query.optimizeOptions(optimizeOptions -> optimizeOptions.disableAll());
        return basicMapper.list(query);
    }

    public void fillFetchData(FetchInfo fetchInfo, List<FetchObject> values, List<Object> fetchData) {
        if (Objects.isNull(fetchData) || fetchData.isEmpty()) {
            return;
        }

        Map<String, List<Object>> map = new HashMap<>();
        fetchData.forEach(item -> {
            Object eqValue;
            try {
                eqValue = fetchInfo.getEqGetFieldInvoker().invoke(item, new Object[]{});
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (Objects.nonNull(eqValue)) {
                List<Object> conditionValues = MapUtil.computeIfAbsent(map, eqValue.toString(), key -> new ArrayList<>());
                if (!conditionValues.contains(item)) {
                    conditionValues.add(item);
                }
            }
        });

        values.forEach(item -> {
            List<Object> matchValues = map.get(item.getMatchKey());
            this.setValue(item.getValue(), matchValues, fetchInfo);
        });
    }

    protected List<Object> fetchData(FetchInfo fetchInfo, List conditionList) {
        if (conditionList.isEmpty()) {
            return Collections.emptyList();
        }
        int batchSize = 100;
        List queryValueList = new ArrayList<>(100);
        Query query = Query.create().returnType(fetchInfo.getReturnType());

        if (Objects.isNull(fetchInfo.getTargetSelectColumn()) || StringPool.EMPTY.equals(fetchInfo.getTargetSelectColumn())) {
            query.select(fetchInfo.getReturnType());
        } else {
            query.select(new Column(fetchInfo.getTargetSelectColumn()));
            if (!fetchInfo.getTargetSelectColumn().contains("(")) {
                query.select(new Column(fetchInfo.getTargetMatchColumn()));
            }
        }
        if (Objects.nonNull(fetchInfo.getOrderBy()) && !StringPool.EMPTY.equals(fetchInfo.getOrderBy())) {
            query.orderBy(OrderByDirection.NONE, fetchInfo.getOrderBy());
        }

        List<Object> resultList = new ArrayList<>(conditionList.size());
        int size = conditionList.size();
        for (int i = 0; i < size; i++) {
            queryValueList.add(conditionList.get(i));
            if (i != 0 && i % batchSize == 0) {
                resultList.addAll(fetchData(fetchInfo, query, (List<Serializable>) queryValueList));
            }
        }
        if (size == 1 || (size - 1) % batchSize != 0) {
            List<Object> list = fetchData(fetchInfo, query, (List<Serializable>) queryValueList);
            if (resultList.isEmpty()) {
                return list;
            }
            resultList.addAll(list);
        }
        return resultList;
    }

    public void singleConditionFetch(Object rowValue, FetchInfo fetchInfo, Object onValue) {
        if (Objects.isNull(basicMapper)) {
            basicMapper = this.configuration.getMapper(BasicMapper.class, SqlSessionThreadLocalUtil.get());
        }
        List<Object> list;
        if (Objects.nonNull(onValue)) {
            list = singleFetchCache.computeIfAbsent(fetchInfo, key -> new HashMap<>()).computeIfAbsent(onValue, key2 -> {
                return this.fetchData(fetchInfo, Collections.singletonList(onValue));
            });
        } else {
            list = new ArrayList<>();
        }
        this.setValue(rowValue, list, fetchInfo);
    }

    protected void setValue(Object rowValue, List<?> matchValues, FetchInfo fetchInfo) {
        if (fetchInfo.isMultiple()) {
            matchValues = Objects.isNull(matchValues) ? new ArrayList<>() : matchValues;
            fetchInfo.setValue(rowValue, matchValues);
        } else {
            if (Objects.isNull(matchValues) || matchValues.isEmpty()) {
                return;
            } else if (matchValues.size() > 1 && !fetchInfo.getFetch().multiValueErrorIgnore()) {
                throw new TooManyResultsException("fetch action found more than 1 record");
            }
            fetchInfo.setValue(rowValue, matchValues.get(0));
        }
    }
}
