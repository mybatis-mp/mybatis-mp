package cn.mybatis.mp.core.mybatis.mapper.mappers;


import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveMethodUtil;
import db.sql.api.Getter;

import java.util.Collection;
import java.util.Objects;

public interface SaveMapper<T> extends BaseMapper, BaseMybatisMapper<T> {

    /**
     * 实体类新增
     *
     * @param entity
     * @return 影响条数
     */
    default int save(T entity) {
        return save(entity, false);
    }

    /**
     * 实体类新增
     *
     * @param entity        实体类实例
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int save(T entity, boolean allFieldForce) {
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), entity, allFieldForce);
    }

    /**
     * 实体类新增
     *
     * @param entity     实体类实例
     * @param saveFields 指定那些列强制插入，null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int save(T entity, Getter<T>... saveFields) {
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), entity, saveFields);
    }


    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @return 插入条数
     */
    default int save(Collection<T> list) {
        return save(list, false);
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 插入条数
     */
    default int save(Collection<T> list, boolean allFieldForce) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), list, allFieldForce);
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     *
     * @param list
     * @return 插入的条数
     */
    default int saveBatch(Collection<T> list) {
        return SaveMethodUtil.saveBatch(getBasicMapper(), getTableInfo(), list);
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
    default int saveBatch(Collection<T> list, Getter<T>... saveFields) {
        return SaveMethodUtil.saveBatch(getBasicMapper(), getTableInfo(), list, saveFields);
    }
}