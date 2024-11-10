package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.MapWithKeyMapperUtil;
import db.sql.api.GetterFun;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

public interface MapWithKeyMapper<T> extends BaseMapper, BaseMybatisMapper<T> {

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <K> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Serializable... ids) {
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), getTableInfo(), mapKey, ids);
    }

    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey map的key
     * @param ids    ids
     * @param <K>    map的key的类型
     * @return 一个map
     */
    default <K, ID extends Serializable> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Collection<ID> ids) {
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), getTableInfo(), mapKey, ids);
    }


    /**
     * 根据多个id查询结果转map
     *
     * @param mapKey   map的key
     * @param consumer where consumer
     * @param <K>      map的key的类型
     * @return 一个map
     */
    default <K> Map<K, T> mapWithKey(GetterFun<T, K> mapKey, Consumer<Where> consumer) {
        return MapWithKeyMapperUtil.mapWithKey(getBasicMapper(), getTableInfo(), mapKey, consumer);
    }

    default <ID extends Serializable> Map<ID, T> map(ID... ids) {
        return MapWithKeyMapperUtil.map(getBasicMapper(), getTableInfo(), ids);
    }

    default <ID extends Serializable, T> Map<ID, T> map(Collection<ID> ids) {
        return MapWithKeyMapperUtil.map(getBasicMapper(), getTableInfo(), ids);
    }
}
