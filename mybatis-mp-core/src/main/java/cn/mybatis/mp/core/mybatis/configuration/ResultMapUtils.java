package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.core.util.FieldUtil;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.Table;
import db.sql.api.impl.tookit.PropertyNamer;
import db.sql.api.impl.tookit.SqlUtil;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.reflect.Field;
import java.util.*;

public final class ResultMapUtils {


    private ResultMapUtils() {

    }

    public static ResultMap getResultMap(MybatisConfiguration configuration, Class clazz) {
        String id = "mp-" + MybatisIdUtil.convertIdPath(clazz.getName());
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
            } else if (Objects.nonNull(clazz.getPackage()) && !clazz.getPackage().getName().startsWith("java")) {
                resultMap = new ResultMap.Builder(configuration, id, clazz, getNormalResultMappings(configuration, clazz), true).build();
                configuration.addResultMap(resultMap);
            }
            return resultMap;
        }
    }

    private static List<ResultMapping> createResultMapping(MybatisConfiguration configuration, boolean isTableId, FieldInfo fieldInfo, String columnName, JdbcType jdbcType, Class<? extends TypeHandler<?>> typeHandler) {
        return Arrays.asList(
                configuration.buildResultMapping(isTableId, fieldInfo, columnName, jdbcType, typeHandler),
                configuration.buildResultMapping(isTableId, fieldInfo, SqlUtil.getAsName(fieldInfo.getClazz(), fieldInfo.getField()), jdbcType, typeHandler)
        );
    }

    private static List<ResultMapping> getEntityResultMappings(MybatisConfiguration configuration, Class entity) {
        TableInfo tableInfo = Tables.get(entity);
        List<ResultMapping> resultMappings = new ArrayList<>(tableInfo.getTableFieldInfos().size() * 2);
        for (TableFieldInfo tableFieldInfo : tableInfo.getTableFieldInfos()) {
            resultMappings.addAll(createResultMapping(configuration, tableFieldInfo.isTableId(), tableFieldInfo.getFieldInfo(), tableFieldInfo.getColumnName(), tableFieldInfo.getTableFieldAnnotation().jdbcType(), tableFieldInfo.getTableFieldAnnotation().typeHandler()));
        }
        return Collections.unmodifiableList(resultMappings);
    }

    /**
     * 普通对象
     *
     * @param configuration
     * @param clazz
     * @return
     */
    private static List<ResultMapping> getNormalResultMappings(MybatisConfiguration configuration, Class clazz) {
        List<Field> list = FieldUtil.getResultMappingFields(clazz);

        List<ResultMapping> resultMappings = new ArrayList<>(list.size() * 4);
        list.forEach(field -> {
            FieldInfo fieldInfo = new FieldInfo(clazz, field);
            resultMappings.add(configuration.buildResultMapping(false, fieldInfo, field.getName(), JdbcType.UNDEFINED, UnknownTypeHandler.class));
            resultMappings.add(configuration.buildResultMapping(false, fieldInfo, PropertyNamer.camelToUnderscore(field.getName()), JdbcType.UNDEFINED, UnknownTypeHandler.class));
            resultMappings.add(configuration.buildResultMapping(false, fieldInfo, SqlUtil.getAsName(clazz, field), JdbcType.UNDEFINED, UnknownTypeHandler.class));
        });

        return Collections.unmodifiableList(resultMappings);
    }

    private static List<ResultMapping> getResultEntityResultMappings(MybatisConfiguration configuration, Class clazz) {
        ResultInfo resultInfo = ResultInfos.get(clazz);

        List<ResultMapping> resultMappings = new ArrayList<>();

        //普通字段（多个） 构建
        resultMappings.addAll(createResultMapping(configuration, resultInfo.getResultFieldInfos()));

        //内嵌字段（多个） 构建
        resultMappings.addAll(createNestedResultMapping(configuration, resultInfo.getNestedResultInfos(), "mp-" + MybatisIdUtil.convertIdPath(clazz.getName())));
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
        List<ResultMapping> resultMappings = new ArrayList<>();
        nestedResultInfos.forEach(item -> resultMappings.add(createNestedResultMapping(configuration, item, path)));
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
        String nestedPath = parentPath + "-" + nestedResultInfo.getField().getName();

        List<ResultMapping> nestedMappings = new ArrayList<>();

        //处理普通字段
        nestedMappings.addAll(createResultMapping(configuration, nestedResultInfo.getResultFieldInfos()));

        nestedMappings.addAll(createNestedResultMapping(configuration, nestedResultInfo.getNestedResultInfos(), nestedPath));

        Class targetType = nestedResultInfo.getFieldInfo().getTypeClass();
        //假如是集合类型
        if (Collection.class.isAssignableFrom(targetType)) {
            List<Class<?>> types = GenericUtil.getGeneric(nestedResultInfo.getField().getGenericType());
            if (Objects.nonNull(types) && !types.isEmpty()) {
                targetType = types.get(0);
            }
        }

        //注册内嵌 ResultMap
        ResultMap resultMap = new ResultMap.Builder(configuration, nestedPath, targetType, nestedMappings, false).build();

        configuration.addResultMap(resultMap);

        //构建内嵌ResultMapping
        return new ResultMapping.Builder(configuration, nestedResultInfo.getField().getName())
                .javaType(nestedResultInfo.getFieldInfo().getTypeClass())
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
        List<ResultMapping> resultMappings = new ArrayList<>();
        //  字段 构建
        resultFieldInfos.stream().filter(ResultFieldInfo::isResultMapping).forEach(item -> {
            if (item instanceof ResultTableFieldInfo) {
                ResultTableFieldInfo resultTableFieldInfo = (ResultTableFieldInfo) item;
                resultMappings.addAll(createResultMapping(configuration, resultTableFieldInfo));
            } else {
                resultMappings.addAll(createResultMapping(configuration, item));
            }
        });
        return resultMappings;
    }

    /**
     * 根据 ResultFieldInfo 构建ResultMapping
     *
     * @param configuration
     * @param resultFieldInfo
     */
    private static List<ResultMapping> createResultMapping(MybatisConfiguration configuration, ResultFieldInfo resultFieldInfo) {
        List<ResultMapping> resultMappingList = new ArrayList<>(5);

        ResultMapping resultMapping = configuration.buildResultMapping(false, resultFieldInfo.getFieldInfo(), resultFieldInfo.getMappingColumnName(), resultFieldInfo.getJdbcType(), resultFieldInfo.getTypeHandler());
        resultMappingList.add(resultMapping);

        if (!SqlUtil.isAsName(resultFieldInfo.getFieldInfo().getClazz(), resultFieldInfo.getFieldInfo().getField(), resultFieldInfo.getMappingColumnName())) {
            String asName = SqlUtil.getAsName(resultFieldInfo.getFieldInfo().getClazz(), resultFieldInfo.getFieldInfo().getField());
            resultMapping = configuration.buildResultMapping(false, resultFieldInfo.getFieldInfo(), asName, resultFieldInfo.getJdbcType(), resultFieldInfo.getTypeHandler());
            resultMappingList.add(resultMapping);
        }
        return resultMappingList;
    }

    /**
     * 根据 ResultTableFieldInfo 构建ResultMapping
     *
     * @param configuration
     * @param resultTableFieldInfo
     * @return
     */
    private static List<ResultMapping> createResultMapping(MybatisConfiguration configuration, ResultTableFieldInfo resultTableFieldInfo) {
        return createResultMapping(configuration, resultTableFieldInfo.getTableFieldInfo().isTableId(), resultTableFieldInfo.getFieldInfo(), resultTableFieldInfo.getMappingColumnName(), resultTableFieldInfo.getJdbcType(), resultTableFieldInfo.getTypeHandler());
    }
}
