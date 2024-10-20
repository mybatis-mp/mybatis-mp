package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.core.NotTableClassException;
import cn.mybatis.mp.core.NotTableFieldException;
import cn.mybatis.mp.core.util.FieldUtil;
import cn.mybatis.mp.core.util.GenericUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.db.annotations.*;
import lombok.Data;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;
import java.util.*;

@Data
public class ResultInfo {

    /**
     * 所有的 FetchInfo 包括内嵌的
     */
    private final Map<Class, List<FetchInfo>> fetchInfoMap;

    /**
     * 所有的 ResultFieldInfo 不包括内嵌的
     */
    private final List<ResultFieldInfo> resultFieldInfos;

    /**
     * 内嵌信息
     */
    private final List<NestedResultInfo> nestedResultInfos;

    /**
     * 表对应的前缀 包括内嵌的
     */
    private final Map<Class, Map<Integer, String>> tablePrefixes;

    public ResultInfo(Class<?> clazz) {
        ParseResult parseResult = parse(clazz);
        this.fetchInfoMap = Collections.unmodifiableMap(parseResult.fetchInfoMap);
        this.resultFieldInfos = Collections.unmodifiableList(parseResult.resultFieldInfos);
        this.tablePrefixes = Collections.unmodifiableMap(parseResult.tablePrefixes);
        this.nestedResultInfos = Collections.unmodifiableList(parseResult.nestedResultInfos);
    }

    private static ParseResult parse(Class<?> clazz) {
        ResultEntity resultEntity = clazz.getAnnotation(ResultEntity.class);
        Objects.requireNonNull(resultEntity);
        ParseResult parseResult = new ParseResult();
        parseResultEntity(parseResult, clazz, resultEntity);
        return parseResult;
    }

    private static void parseResultEntity(ParseResult parseResult, Class<?> clazz, ResultEntity resultEntity) {
        TableInfo resultEntityTableInfo = resultEntity.value().isAnnotationPresent(Table.class) ? Tables.get(resultEntity.value()) : null;

        int tableCount = 0;
        if (Objects.nonNull(resultEntityTableInfo)) {
            tableCount = createPrefix(resultEntity.value(), resultEntity.storey(), parseResult.tablePrefixes, 0);
        } else if (resultEntity.value() != Void.TYPE && resultEntity.value() != Void.class) {
            throw new NotTableClassException(resultEntity.value());
        }

        List<Field> fieldList = FieldUtil.getResultMappingFields(clazz);
        for (Field field : fieldList) {
            if (field.isAnnotationPresent(ResultField.class)) {
                //普通字段
                ResultField resultField = field.getAnnotation(ResultField.class);
                parseResult.resultFieldInfos.add(new ResultFieldInfo(field, resultField));
                continue;
            }

            if (field.isAnnotationPresent(Fetch.class)) {
                //Fetch
                tableCount = parseFetch(parseResult, parseResult.resultFieldInfos, clazz, field, tableCount);
                continue;
            }

            if (field.isAnnotationPresent(NestedResultEntity.class)) {
                //内嵌类字段
                NestedResultEntity nestedResultEntity = field.getAnnotation(NestedResultEntity.class);
                NestedResultInfo nestedResultInfo = new NestedResultInfo(field, nestedResultEntity, new ArrayList<>(), new ArrayList<>());

                parseResult.nestedResultInfos.add(nestedResultInfo);

                tableCount = parseNestedResultEntity(parseResult, nestedResultInfo, field, nestedResultEntity, tableCount);
                continue;
            }


            if (Objects.isNull(resultEntityTableInfo)) {
                throw new RuntimeException("the class:" + clazz + "'s @ResultEntity not set correct value");
            }

            TableInfo tableInfo;
            TableFieldInfo tableFieldInfo;
            String tableFieldName;
            Class<?> entity;
            Integer storey;

            if (field.isAnnotationPresent(ResultEntityField.class)) {
                ResultEntityField resultEntityField = field.getAnnotation(ResultEntityField.class);
                if (resultEntityField.target() != Void.class) {
                    entity = resultEntityField.target();
                    if (resultEntity.storey() != resultEntityField.storey()) {
                        throw new RuntimeException(" class:" + clazz.getName() + ", the field:" + field.getName() + " config @ResultEntityField(storey) error");
                    }
                    storey = resultEntityField.storey();
                    tableInfo = Tables.get(entity);
                    if (Objects.isNull(tableInfo)) {
                        throw new NotTableClassException(entity);
                    }
                } else {
                    entity = resultEntity.value();
                    storey = resultEntity.storey();
                    tableInfo = resultEntityTableInfo;
                }
                tableFieldName = resultEntityField.property();
                if (tableFieldName.isEmpty()) {
                    tableFieldName = field.getName();
                }
            } else {
                tableInfo = resultEntityTableInfo;
                entity = resultEntity.value();
                storey = resultEntity.storey();
                tableFieldName = field.getName();
            }

            tableFieldInfo = tableInfo.getFieldInfo(tableFieldName);
            if (Objects.isNull(tableFieldInfo)) {
                throw new NotTableFieldException(entity, tableFieldName);
            }

            if (resultEntity.value() != entity || resultEntity.storey() != storey) {
                tableCount = createPrefix(entity, storey, parseResult.tablePrefixes, tableCount);
            }

            //获取前缀
            String tablePrefix = getTablePrefix(parseResult.tablePrefixes, entity, storey);
            //表字段
            parseResult.resultFieldInfos.add(new ResultTableFieldInfo(entity, storey, tablePrefix, tableInfo, tableFieldInfo, field));
        }
    }

    /**
     * 解析内嵌字段
     *
     * @param parseResult        解析结果
     * @param nestedResultInfo   内嵌信息
     * @param sourceField        字段
     * @param nestedResultEntity 内嵌注解
     * @param tableCount         当前表个数
     * @return 当前已存在表的个数
     */
    private static int parseNestedResultEntity(ParseResult parseResult, NestedResultInfo nestedResultInfo, Field sourceField, NestedResultEntity nestedResultEntity, int tableCount) {
        //添加前缀
        tableCount = createPrefix(nestedResultEntity.target(), nestedResultEntity.storey(), parseResult.tablePrefixes, tableCount);

        Class targetType = nestedResultInfo.getField().getType();
        //假如是集合类型
        if (Collection.class.isAssignableFrom(targetType)) {
            List<Class<?>> types = GenericUtil.getGeneric(nestedResultInfo.getField().getGenericType());
            if (Objects.nonNull(types) && !types.isEmpty()) {
                targetType = types.get(0);
            }
        }

        //是否隐射的实体类
        boolean fieldTypeIsEntity = targetType.isAnnotationPresent(Table.class);

        TableInfo tableInfo = Tables.get(nestedResultEntity.target());
        if (Objects.isNull(tableInfo)) {
            throw new NotTableClassException(nestedResultEntity.target());
        }

        for (Field field : FieldUtil.getResultMappingFields(targetType)) {
            if (field.isAnnotationPresent(ResultField.class)) {
                //普通字段
                ResultField resultField = field.getAnnotation(ResultField.class);
                nestedResultInfo.getResultFieldInfos().add(new ResultFieldInfo(field, resultField));
                continue;
            }

            if (field.isAnnotationPresent(Fetch.class)) {
                //Fetch
                Class fetchType = Collection.class.isAssignableFrom(sourceField.getType()) ? GenericUtil.getGeneric(sourceField.getGenericType()).get(0) : sourceField.getType();

                tableCount = parseFetch(parseResult, nestedResultInfo.getResultFieldInfos(), fetchType, field, tableCount);
                continue;
            }

            if (field.isAnnotationPresent(ResultEntityField.class)) {
                ResultEntityField resultEntityField = field.getAnnotation(ResultEntityField.class);
                if (resultEntityField.target() == Void.class) {
                    throw new RuntimeException(" class:" + field.getDeclaringClass() + ", the field:" + field.getName() + " config @ResultEntityField error");
                }

                Class<?> entity = resultEntityField.target();
                int storey = resultEntityField.storey();

                tableInfo = Tables.get(entity);
                if (Objects.isNull(tableInfo)) {
                    throw new NotTableClassException(entity);
                }

                String tableFieldName = resultEntityField.property();
                if (tableFieldName.isEmpty()) {
                    tableFieldName = field.getName();
                }

                TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(tableFieldName);
                if (Objects.isNull(tableFieldInfo)) {
                    throw new NotTableFieldException(entity, tableFieldName);
                }

                tableCount = createPrefix(entity, storey, parseResult.tablePrefixes, tableCount);

                //获取前缀
                String tablePrefix = getTablePrefix(parseResult.tablePrefixes, entity, storey);

                //表字段
                nestedResultInfo.getResultFieldInfos().add(new ResultTableFieldInfo(entity, storey, tablePrefix, tableInfo, tableFieldInfo, field));
                continue;
            }


            if (field.isAnnotationPresent(NestedResultEntity.class)) {
                //内嵌类字段
                NestedResultEntity newNestedResultEntity = field.getAnnotation(NestedResultEntity.class);

                NestedResultInfo newNestedResultInfo = new NestedResultInfo(field, newNestedResultEntity, new ArrayList<>(), new ArrayList<>());
                nestedResultInfo.getNestedResultInfos().add(newNestedResultInfo);

                tableCount = parseNestedResultEntity(parseResult, newNestedResultInfo, field, newNestedResultEntity, tableCount);
                continue;
            }

            String targetFieldName = field.getName();
            NestedResultEntityField nestedResultEntityField = field.getAnnotation(NestedResultEntityField.class);
            if (Objects.nonNull(nestedResultEntityField)) {
                targetFieldName = nestedResultEntityField.value();
            }

            TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(targetFieldName);
            if (Objects.isNull(nestedResultEntityField) && fieldTypeIsEntity) {
                if (!tableFieldInfo.getTableFieldAnnotation().select()) {
                    continue;
                }
            }
            if (Objects.isNull(tableFieldInfo)) {
                throw new NotTableFieldException(nestedResultEntity.target(), field.getName());
            }

            //获取前缀
            String tablePrefix = getTablePrefix(parseResult.tablePrefixes, nestedResultEntity.target(), nestedResultEntity.storey());

            //表字段
            nestedResultInfo.getResultFieldInfos().add(new ResultTableFieldInfo(nestedResultEntity.target(), nestedResultEntity.storey(), tablePrefix, tableInfo, tableFieldInfo, field));
        }

        return tableCount;
    }

    private static RuntimeException buildException(Class clazz, Field field, String annotationName, String annotationPropertyName, String message) {
        return new RuntimeException(clazz.getName() + "." + field.getName() + " " + annotationName + "  config error,the " + annotationPropertyName + ":" + message);
    }

    private static String parseOrderByColumn(Class clazz, Field field, TableInfo targetTableInfo, String annotationName, String annotationPropertyName, String annotationValue) {
        String value = annotationValue.trim();
        if (value.isEmpty()) {
            return null;
        }
        if (value.startsWith("[") && value.endsWith("]")) {
            return parseDynamicColumn(clazz, field, targetTableInfo, annotationName, annotationPropertyName, value);
        }

        StringBuilder orderByJoin = new StringBuilder();
        String[] strs = value.split(",");
        for (int i = 0; i < strs.length; i++) {
            String str = strs[i];
            String[] ss = str.trim().split(" ");
            if (ss.length > 2) {
                throw buildException(clazz, field, annotationName, annotationPropertyName, "format error");
            }
            if (StringPool.EMPTY.equals(ss[0])) {
                throw buildException(clazz, field, annotationName, annotationPropertyName, "format error");
            }
            TableFieldInfo orderByTableFieldInfo = targetTableInfo.getFieldInfo(ss[0]);
            if (Objects.isNull(orderByTableFieldInfo)) {
                throw buildException(clazz, field, annotationName, annotationPropertyName, " the field:" + ss[0] + " is not entity field");
            }
            if (i != 0) {
                orderByJoin.append(",");
            }
            orderByJoin.append(orderByTableFieldInfo.getColumnName()).append(" ").append(ss[1]);
        }
        return orderByJoin.toString();
    }

    private static String parseDynamicColumn(Class clazz, Field field, TableInfo targetTableInfo, String annotationName, String annotationPropertyName, String annotationValue) {
        String value = annotationValue.trim();
        if (value.isEmpty()) {
            return null;
        }
        if (value.startsWith("[") && value.endsWith("]")) {
            StringBuilder targetSelectColumnBuilder = new StringBuilder();
            int startIndex = 1;
            while (true) {
                int start = value.indexOf("{", startIndex);
                if (start == -1) {
                    if (targetSelectColumnBuilder.length() == 0) {
                        throw buildException(clazz, field, annotationName, annotationPropertyName, "format error");
                    } else {
                        targetSelectColumnBuilder.append(value.substring(startIndex, value.length() - 1));
                        return targetSelectColumnBuilder.toString();
                    }
                }
                int end = value.indexOf("}", start);
                if (end == -1) {
                    throw buildException(clazz, field, annotationName, annotationPropertyName, "format error");
                }
                String property = value.substring(start + 1, end);
                TableFieldInfo targetSelectTargetFieldInfo = targetTableInfo.getFieldInfo(property);
                if (Objects.isNull(targetSelectTargetFieldInfo)) {
                    throw buildException(clazz, field, annotationName, annotationPropertyName, property + " is not a entity field");
                }
                targetSelectColumnBuilder.append(value.substring(startIndex, start)).append(targetSelectTargetFieldInfo.getColumnName());
                startIndex = end + 1;
            }
        }

        TableFieldInfo targetSelectTargetFieldInfo = targetTableInfo.getFieldInfo(value);
        if (Objects.isNull(targetSelectTargetFieldInfo)) {
            throw buildException(clazz, field, annotationName, annotationPropertyName, value + " is not a entity field");
        }
        return targetSelectTargetFieldInfo.getColumnName();

    }

    /**
     * 解析内嵌字段
     *
     * @param parseResult 解析结果
     * @param field       字段
     * @param tableCount  当前表个数
     * @return 当前已存在表的个数
     */
    private static int parseFetch(ParseResult parseResult, List<ResultFieldInfo> resultFieldInfos, Class clazz, Field field, int tableCount) {
        Fetch fetch = field.getAnnotation(Fetch.class);

        String valueColumn = fetch.column();
        TypeHandler<?> valueTypeHandler = null;
        if (StringPool.EMPTY.equals(valueColumn)) {
            if (!fetch.source().isAnnotationPresent(Table.class)) {
                throw new RuntimeException(clazz.getName() + "->" + field.getName() + " fetch config error,the source: " + fetch.source().getName() + " is not a entity");
            }
            TableInfo fetchTableInfo = Tables.get(fetch.source());
            TableFieldInfo fetchFieldInfo = fetchTableInfo.getFieldInfo(fetch.property());

            if (Objects.isNull(fetchFieldInfo)) {
                throw new RuntimeException(clazz.getName() + "->" + field.getName() + " fetch config error,the property: " + fetch.property() + " is not a entity field");
            }
            valueTypeHandler = fetchFieldInfo.getTypeHandler();
            //以字段为基础的查询
            //创建前缀
            tableCount = createPrefix(fetch.source(), fetch.storey(), parseResult.tablePrefixes, tableCount);
            //获取前缀
            String tablePrefix = getTablePrefix(parseResult.tablePrefixes, fetch.source(), fetch.storey());

            resultFieldInfos.add(new ResultTableFieldInfo(false, fetch.source(), fetch.storey(), tablePrefix, fetchTableInfo, fetchFieldInfo, field));
            valueColumn = tablePrefix + fetchFieldInfo.getColumnName();

        }

        if (StringPool.EMPTY.equals(fetch.targetProperty())) {
            throw buildException(clazz, field, "@Fetch", "targetProperty", "can't be empty");
        }

        if (!fetch.target().isAnnotationPresent(Table.class)) {
            throw buildException(clazz, field, "@Fetch", "target", fetch.target().getName() + " is not a entity");
        }

        TableInfo fetchTargetTableInfo = Tables.get(fetch.target());
        TableFieldInfo fetchTargetFieldInfo = fetchTargetTableInfo.getFieldInfo(fetch.targetProperty());

        if (Objects.isNull(fetchTargetFieldInfo)) {
            throw buildException(clazz, field, "@Fetch", "targetProperty", fetch.targetProperty() + " is not a entity field");
        }

        String targetSelectColumn = parseDynamicColumn(clazz, field, fetchTargetTableInfo, "@Fetch", "targetSelectProperty", fetch.targetSelectProperty());

        String orderBy = parseOrderByColumn(clazz, field, fetchTargetTableInfo, "@Fetch", "orderBy", fetch.orderBy());

        String targetMatchColumn = fetchTargetFieldInfo.getColumnName();

        Class returnType = Collection.class.isAssignableFrom(field.getType()) ? GenericUtil.getGeneric(field.getGenericType()).get(0) : field.getType();

        Field targetMatchField = null;
        if (returnType.isAnnotationPresent(ResultEntity.class)) {
            ResultInfo resultInfo = ResultInfos.get(returnType);
            Optional<Field> eqFieldOptional = resultInfo.getResultFieldInfos().stream()
                    .filter(item -> item instanceof ResultTableFieldInfo)
                    .filter(item -> ((ResultTableFieldInfo) item).getTableFieldInfo().getField() == fetchTargetFieldInfo.getField())
                    .map(item -> item.getField())
                    .findFirst();
            if (eqFieldOptional.isPresent()) {
                targetMatchField = eqFieldOptional.get();
            }
        } else if (returnType.isAnnotationPresent(Table.class)) {
            if (returnType != fetchTargetTableInfo.getType()) {
                throw new RuntimeException(clazz.getName() + "->" + field.getName() + " fetch config error,the type can't" + returnType.getName());
            }
            targetMatchField = fetchTargetFieldInfo.getField();
        }

        parseResult.fetchInfoMap.computeIfAbsent(clazz, key -> new ArrayList<>()).add(new FetchInfo(field, fetch, returnType, valueColumn, valueTypeHandler, targetMatchField, targetMatchColumn, targetSelectColumn, orderBy));
        return tableCount;
    }

    /**
     * 返回 新的index
     *
     * @param entity          实体类
     * @param storey          存储层级
     * @param entityPrefixMap 实体类前缀 map
     * @param tableCount      当前表个数
     * @return
     */
    private static int createPrefix(Class entity, int storey, Map<Class, Map<Integer, String>> entityPrefixMap, int tableCount) {
        if (Objects.nonNull(entity)) {
            tableCount++;
            String prefix;
            if (tableCount == 1) {
                prefix = "";
            } else {
                prefix = "x" + tableCount + "$";
            }
            if (!addPrefix(entityPrefixMap, entity, storey, prefix)) {
                tableCount--;
            }
        }

        return tableCount;
    }

    private static boolean addPrefix(Map<Class, Map<Integer, String>> entityPrefixMap, Class<?> entityType, int storey, String prefix) {
        Map<Integer, String> prefixMap = entityPrefixMap.computeIfAbsent(entityType, key -> new HashMap<>());
        if (prefixMap.containsKey(storey)) {
            return false;
        }
        prefixMap.put(storey, prefix);
        return true;
    }

    /**
     * 获取表的前缀
     *
     * @param entityPrefixMap
     * @param entity          实体类的类型
     * @param storey          实体类存储层级
     * @return 前缀
     */
    private static String getTablePrefix(Map<Class, Map<Integer, String>> entityPrefixMap, Class<?> entity, int storey) {
        String prefix = entityPrefixMap.get(entity).get(storey);
        Objects.requireNonNull(prefix);
        return prefix;
    }

    public List<ResultFieldInfo> getResultFieldInfos() {
        return resultFieldInfos;
    }

    public List<NestedResultInfo> getNestedResultInfos() {
        return nestedResultInfos;
    }

    public Map<Class, List<FetchInfo>> getFetchInfoMap() {
        return fetchInfoMap;
    }

    public Map<Class, Map<Integer, String>> getTablePrefixes() {
        return tablePrefixes;
    }

    static class ParseResult {

        public final Map<Class, List<FetchInfo>> fetchInfoMap = new HashMap<>();

        public final List<ResultFieldInfo> resultFieldInfos = new ArrayList<>();

        public final List<NestedResultInfo> nestedResultInfos = new ArrayList<>();

        public final Map<Class, Map<Integer, String>> tablePrefixes = new HashMap<>();
    }
}
