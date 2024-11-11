package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;


import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveMethodUtil;
import db.sql.api.Getter;

import java.util.Collection;

public interface SaveBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 实体类新增
     *
     * @param entity
     * @return 影响条数
     */
    default <T> int save(T entity) {
        return save(entity, false);
    }

    /**
     * 实体类新增
     *
     * @param entity        实体类实例
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 影响条数
     */
    default <T> int save(T entity, boolean allFieldForce) {
        return SaveMethodUtil.save(getBasicMapper(), Tables.get(entity.getClass()), entity, allFieldForce);
    }

    /**
     * 实体类新增
     *
     * @param entity     实体类实例
     * @param saveFields 指定那些列强制插入，null值将会以NULL的形式插入
     * @return 影响条数
     */
    default <T> int save(T entity, Getter<T>... saveFields) {
        return SaveMethodUtil.save(getBasicMapper(), Tables.get(entity.getClass()), entity, saveFields);
    }


    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @return 插入条数
     */
    default <T> int save(Collection<T> list) {
        return save(list, false);
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 插入条数
     */
    default <T> int save(Collection<T> list, boolean allFieldForce) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return SaveMethodUtil.save(getBasicMapper(), Tables.get(first.getClass()), list, allFieldForce);
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     *
     * @param list
     * @return 插入的条数
     */
    default <T> int saveBatch(Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return SaveMethodUtil.saveBatch(getBasicMapper(), Tables.get(first.getClass()), list);
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     * <p>
     * 会自动加入 主键 租户ID 逻辑删除列 乐观锁
     * 自动设置 默认值,不会忽略NULL值字段
     *
     * @param list
     * @param saveFields 指定那些列强制插入，null值将会以NULL的形式插入
     * @return 插入的条数
     */
    default <T> int saveBatch(Collection<T> list, Getter<T>... saveFields) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return SaveMethodUtil.saveBatch(getBasicMapper(), Tables.get(first.getClass()), list, saveFields);
    }
}