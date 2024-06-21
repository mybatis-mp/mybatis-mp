package cn.mybatis.mp.core.mybatis.configuration;

import cn.mybatis.mp.core.mybatis.typeHandler.GenericTypeHandler;
import cn.mybatis.mp.core.mybatis.typeHandler.MybatisTypeHandlerUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ResultMapWrapper {

    public static void replaceResultMap(MappedStatement ms) {
        if (ms.getSqlCommandType() != SqlCommandType.SELECT) {
            return;
        }
        MetaObject msMetaObject = ms.getConfiguration().newMetaObject(ms);
        msMetaObject.setValue("resultMaps", replaceResultMap((MybatisConfiguration) ms.getConfiguration(), ms.getResultMaps()));
    }

    public static List<ResultMap> replaceResultMap(MybatisConfiguration configuration, List<ResultMap> sourceResultMap) {
        return sourceResultMap.stream().map(item -> {
            if (!item.getResultMappings().isEmpty()) {
                return replaceTypeHandler(configuration, item);
            }
            String resultMapId = "mp-" + item.getType().getName();
            if (configuration.hasResultMap(resultMapId)) {
                return configuration.getResultMap(resultMapId);
            }
            ResultMap newResultMap = ResultMapUtils.getResultMap(configuration, item.getType());
            if (Objects.nonNull(newResultMap)) {
                return newResultMap;
            }
            return item;
        }).collect(Collectors.toList());
    }

    private static ResultMap replaceTypeHandler(MybatisConfiguration configuration, ResultMap resultMap) {
        if (Map.class.isAssignableFrom(resultMap.getType())) {
            return resultMap;
        }

        MetaObject msMetaObject = configuration.newMetaObject(resultMap);
        msMetaObject.setValue("resultMappings", replaceTypeHandler(configuration, resultMap, resultMap.getResultMappings()));
        msMetaObject.setValue("propertyResultMappings", replaceTypeHandler(configuration, resultMap, resultMap.getPropertyResultMappings()));
        msMetaObject.setValue("idResultMappings", replaceTypeHandler(configuration, resultMap, resultMap.getIdResultMappings()));
        return resultMap;
    }

    private static List<ResultMapping> replaceTypeHandler(MybatisConfiguration configuration, ResultMap resultMap, List<ResultMapping> resultMappings) {
        return resultMappings.stream().map(item -> {
            ResultMapping newItem = item;
            TypeHandler<?> typeHandler = item.getTypeHandler();
            if (typeHandler instanceof GenericTypeHandler) {
                Field field;
                try {
                    field = resultMap.getType().getDeclaredField(item.getProperty());
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }

                //有泛型 需要替换mybatis-mp的实例
                typeHandler = MybatisTypeHandlerUtil.getTypeHandler(configuration, field, (Class<? extends TypeHandler<?>>) typeHandler.getClass(), item.getJdbcType());
                newItem = new ResultMapping.Builder(configuration, item.getProperty(), item.getColumn(), typeHandler)
                        .javaType(field.getType())
                        .jdbcType(item.getJdbcType())
                        .nestedResultMapId(item.getNestedResultMapId())
                        .nestedQueryId(item.getNestedQueryId())
                        .notNullColumns(item.getNotNullColumns())
                        .columnPrefix(item.getColumnPrefix())
                        .flags(item.getFlags())
                        .composites(item.getComposites())
                        .resultSet(item.getResultSet())
                        .foreignColumn(item.getForeignColumn())
                        .lazy(item.isLazy())
                        .build();
            }

            return newItem;
        }).collect(Collectors.toList());
    }

}
