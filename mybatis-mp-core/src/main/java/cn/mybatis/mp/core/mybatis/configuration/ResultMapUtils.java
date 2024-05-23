package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.Table;
import db.sql.api.impl.tookit.SqlUtil;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.JdbcType;

import java.util.*;
import java.util.stream.Collectors;

public final class ResultMapUtils {

    private ResultMapUtils() {

    }

    public static ResultMap getResultMap(MybatisConfiguration configuration, Class clazz) {
        String id = "mp-" + clazz.getSimpleName();
        if (configuration.hasResultMap(id)) {
            return configuration.getResultMap(id);
        }

        synchronized (clazz) {
            if (configuration.hasResultMap(id)) {
                return configuration.getResultMap(id);
            }

            List<ResultMapping> resultMappings = null;
            if (clazz.isAnnotationPresent(Table.class)) {
                resultMappings = getEntityResultMappings(configuration, clazz);
            } else if (clazz.isAnnotationPresent(ResultEntity.class)) {
                resultMappings = getResultEntityResultMappings(configuration, clazz);
            }
            ResultMap resultMap = null;
            if (Objects.nonNull(resultMappings)) {
                resultMap = new ResultMap.Builder(configuration, id, clazz, resultMappings, false).build();
                configuration.addResultMap(resultMap);
            } else if (Map.class.isAssignableFrom(clazz)) {
                resultMap = new ResultMap.Builder(configuration, id, clazz, Collections.emptyList(), true).build();
                configuration.addResultMap(resultMap);
            }
            return resultMap;
        }
    }

    private static List<ResultMapping> getEntityResultMappings(MybatisConfiguration configuration, Class entity) {
        TableInfo tableInfo = Tables.get(entity);
        List<ResultMapping> resultMappings = tableInfo.getTableFieldInfos().stream().map(tableFieldInfo -> configuration.buildResultMapping(tableFieldInfo.isTableId(), tableFieldInfo.getField(), tableFieldInfo.getColumnName(), tableFieldInfo.getTableFieldAnnotation().jdbcType(), tableFieldInfo.getTableFieldAnnotation().typeHandler())).collect(Collectors.toList());
        return Collections.unmodifiableList(resultMappings);
    }

    private static List<ResultMapping> getResultEntityResultMappings(MybatisConfiguration configuration, Class clazz) {
        ResultInfo resultInfo = ResultInfos.get(clazz);

        List<ResultMapping> resultMappings = new LinkedList<>();

        //普通字段（多个） 构建
        resultMappings.addAll(createResultMapping(configuration, resultInfo.getResultFieldInfos()));

        //内嵌字段（多个） 构建
        resultMappings.addAll(createNestedResultMapping(configuration, resultInfo.getNestedResultInfos(), "mp-" + clazz.getSimpleName()));
        return Collections.unmodifiableList(resultMappings);
    }

    /**
     * 多个内嵌构建
     *
     * @param configuration
     * @param nestedResultInfos
     * @return
     */
    private static List<ResultMapping> createNestedResultMapping(MybatisConfiguration configuration, List<NestedResultInfo> nestedResultInfos, String path) {
        List<ResultMapping> resultMappings = new LinkedList<>();
        nestedResultInfos.stream().forEach(item -> {
            resultMappings.add(createNestedResultMapping(configuration, item, path));
        });
        return resultMappings;
    }

    /**
     * 单个内嵌构建
     *
     * @param configuration
     * @param nestedResultInfo
     * @return
     */
    private static ResultMapping createNestedResultMapping(MybatisConfiguration configuration, NestedResultInfo nestedResultInfo, String parentPath) {

        // 内嵌ID
        String nestedPath = parentPath + "." + nestedResultInfo.getField().getName();

        List<ResultMapping> nestedMappings = new LinkedList<>();

        //处理普通字段
        nestedMappings.addAll(createResultMapping(configuration, nestedResultInfo.getResultFieldInfos()));

        nestedMappings.addAll(createNestedResultMapping(configuration, nestedResultInfo.getNestedResultInfos(), nestedPath));

        Class targetType = nestedResultInfo.getField().getType();
        //假如是集合类型
        if (Collection.class.isAssignableFrom(targetType)) {
            List<Class> types = GenericUtil.getGeneric(nestedResultInfo.getField().getGenericType());
            if (Objects.nonNull(types) && !types.isEmpty()) {
                targetType = types.get(0);
            }
        }

        //注册内嵌 ResultMap
        ResultMap resultMap = new ResultMap.Builder(configuration, nestedPath, targetType, nestedMappings, false).build();

        configuration.addResultMap(resultMap);

        //构建内嵌ResultMapping
        return new ResultMapping.Builder(configuration, nestedResultInfo.getField().getName())
                .javaType(nestedResultInfo.getField().getType())
                .jdbcType(JdbcType.UNDEFINED)
                .nestedResultMapId(nestedPath).build();
    }


    /**
     * 多个ResultFieldInfo 构建ResultMapping
     *
     * @param configuration
     * @param resultFieldInfos
     * @return
     */
    private static List<ResultMapping> createResultMapping(MybatisConfiguration configuration, List<ResultFieldInfo> resultFieldInfos) {
        List<ResultMapping> resultMappings = new LinkedList<>();
        //  字段 构建
        resultFieldInfos.stream().filter(item -> item.isResultMapping())
                .forEach(item -> {
                    if (item instanceof ResultTableFieldInfo) {
                        ResultTableFieldInfo resultTableFieldInfo = (ResultTableFieldInfo) item;
                        resultMappings.add(createResultMapping(configuration, resultTableFieldInfo));
                    } else {
                        createResultMapping(configuration, item, resultMappings);
                    }
                });
        return resultMappings;
    }

    /**
     * 根据 ResultFieldInfo 构建ResultMapping
     *
     * @param configuration
     * @param resultFieldInfo
     * @param resultMappings
     */
    private static void createResultMapping(MybatisConfiguration configuration, ResultFieldInfo resultFieldInfo, List<ResultMapping> resultMappings) {
        ResultMapping resultMapping = configuration.buildResultMapping(false, resultFieldInfo.getField(), resultFieldInfo.getMappingColumnName(), resultFieldInfo.getJdbcType(), resultFieldInfo.getTypeHandler());
        resultMappings.add(resultMapping);

        if (!SqlUtil.isAsName(resultFieldInfo.getField(), resultFieldInfo.getMappingColumnName())) {
            String asName = SqlUtil.getAsName(resultFieldInfo.getField());
            resultMapping = configuration.buildResultMapping(false, resultFieldInfo.getField(), asName, resultFieldInfo.getJdbcType(), resultFieldInfo.getTypeHandler());
            resultMappings.add(resultMapping);
        }
    }

    /**
     * 根据 ResultTableFieldInfo 构建ResultMapping
     *
     * @param configuration
     * @param resultTableFieldInfo
     * @return
     */
    private static ResultMapping createResultMapping(MybatisConfiguration configuration, ResultTableFieldInfo resultTableFieldInfo) {
        boolean isId = resultTableFieldInfo.getTableFieldInfo().isTableId();
        return configuration.buildResultMapping(isId, resultTableFieldInfo.getField(), resultTableFieldInfo.getMappingColumnName(), resultTableFieldInfo.getJdbcType(), resultTableFieldInfo.getTypeHandler());
    }
}
