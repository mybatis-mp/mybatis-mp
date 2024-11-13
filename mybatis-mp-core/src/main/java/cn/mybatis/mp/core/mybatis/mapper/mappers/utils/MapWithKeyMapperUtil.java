package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.sql.util.QueryUtil;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.GetterFun;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.LambdaUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public final class MapWithKeyMapperUtil {


    public static <T, K> Map<K, T> mapWithKey(BasicMapper basicMapper, TableInfo tableInfo, String mapKey, Consumer<Where> consumer) {
        return basicMapper.mapWithKey(mapKey, QueryUtil.buildNoOptimizationQuery(tableInfo, WhereUtil.create(tableInfo, consumer), q -> QueryUtil.fillQueryDefault(q, tableInfo)));
    }

    public static <T, K, ID extends Serializable> Map<K, T> mapWithKey(BasicMapper basicMapper, TableInfo tableInfo, String mapKey, ID[] ids) {
        return mapWithKey(basicMapper, tableInfo, mapKey, where -> WhereUtil.appendIdsWhere(where, tableInfo, ids));
    }

    public static <T, K, ID extends Serializable> Map<K, T> mapWithKey(BasicMapper basicMapper, TableInfo tableInfo, String mapKey, Collection<ID> ids) {
        return mapWithKey(basicMapper, tableInfo, mapKey, where -> WhereUtil.appendIdsWhere(where, tableInfo, ids));
    }


    public static <T, K> Map<K, T> mapWithKey(BasicMapper basicMapper, TableInfo tableInfo, GetterFun<T, K> mapKey, Consumer<Where> consumer) {
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(mapKey);
        return basicMapper.mapWithKey(lambdaFieldInfo.getName(),
                QueryUtil.buildNoOptimizationQuery(tableInfo, WhereUtil.create(tableInfo, consumer), q -> {
                    q.setReturnType(lambdaFieldInfo.getType());
                    QueryUtil.fillQueryDefault(q, tableInfo);
                })
        );
    }

    public static <T, K, ID extends Serializable> Map<K, T> mapWithKey(BasicMapper basicMapper, TableInfo tableInfo, GetterFun<T, K> mapKey, ID[] ids) {
        if (Objects.isNull(ids) || ids.length == 0) {
            return Collections.emptyMap();
        }
        return mapWithKey(basicMapper, tableInfo, mapKey, where -> WhereUtil.appendIdsWhere(where, tableInfo, ids));
    }

    public static <T, K, ID extends Serializable> Map<K, T> mapWithKey(BasicMapper basicMapper, TableInfo tableInfo, GetterFun<T, K> mapKey, Collection<ID> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return mapWithKey(basicMapper, tableInfo, mapKey, where -> WhereUtil.appendIdsWhere(where, tableInfo, ids));
    }

    public static <ID extends Serializable, T> Map<ID, T> map(BasicMapper basicMapper, TableInfo tableInfo, ID[] ids) {
        if (Objects.isNull(ids) || ids.length == 0) {
            return Collections.emptyMap();
        }
        return basicMapper.mapWithKey(tableInfo.getSingleIdFieldInfo(true).getField().getName(), QueryUtil.buildIdsQuery(tableInfo, ids));
    }

    public static <ID extends Serializable, T> Map<ID, T> map(BasicMapper basicMapper, TableInfo tableInfo, Collection<ID> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return Collections.emptyMap();
        }
        return basicMapper.mapWithKey(tableInfo.getSingleIdFieldInfo(true).getField().getName(), QueryUtil.buildIdsQuery(tableInfo, ids));
    }
}
