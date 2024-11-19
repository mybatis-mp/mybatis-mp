package cn.mybatis.mp.core.mybatis.mapper.mappers;


import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveOrUpdateMethodUtil;
import db.sql.api.Getter;

import java.util.Collection;

public interface SaveOrUpdateMapper<T> extends BaseMapper<T> {

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity 实体类对象
     * @return 影响条数
     */
    default int saveOrUpdate(T entity) {
        return saveOrUpdate(entity, false);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @param allFieldForce 所有字段都强制保存或修改,null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int saveOrUpdate(T entity, boolean allFieldForce) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), entity, allFieldForce);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @param forceFields 强制字段
     * @return 影响条数
     */
    default int saveOrUpdate(T entity, Getter<T>... forceFields) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), entity, false, forceFields);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    default int saveOrUpdate(Collection<T> list) {
        return saveOrUpdate(list, false);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list          实体类对象List
     * @param allFieldForce 所有字段都强制保存或修改,null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int saveOrUpdate(Collection<T> list, boolean allFieldForce) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), list, allFieldForce, (Getter<T>[]) null);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list        实体类对象List
     * @param forceFields 强制字段
     * @return 影响条数
     */
    default int saveOrUpdate(Collection<T> list, Getter<T>... forceFields) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), list, false, forceFields);
    }
}
