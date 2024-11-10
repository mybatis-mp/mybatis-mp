package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.QueryUtil;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.MapWithKeyMapperUtil;
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

public interface MapWithKeyBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <T, K, ID extends Serializable> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, ID... ids) {
        if (Objects.isNull(ids) || ids.length < 1) {
            return Collections.emptyMap();
        }
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(mapKey);
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), Tables.get(lambdaFieldInfo.getType()), lambdaFieldInfo.getName(), ids);
    }

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <T, K, ID extends Serializable> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Collection<ID> ids) {
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return Collections.emptyMap();
        }
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(mapKey);
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), Tables.get(lambdaFieldInfo.getType()), lambdaFieldInfo.getName(), ids);
    }


    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey   map的key
     * @param consumer where consumer
     * @param <K>      map的key的类型
     * @return 一个map
     */
    default <T, K> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Consumer<Where> consumer) {
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(mapKey);
        TableInfo tableInfo = Tables.get(lambdaFieldInfo.getType());
        return this.mapWithKey(lambdaFieldInfo.getName(), QueryUtil.buildNoOptimizationQuery(tableInfo, WhereUtil.create(tableInfo, consumer)));
    }
}
