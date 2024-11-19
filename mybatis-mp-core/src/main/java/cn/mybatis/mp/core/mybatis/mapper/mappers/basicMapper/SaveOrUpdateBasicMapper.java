package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;


import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveOrUpdateMethodUtil;
import db.sql.api.Getter;

import java.util.Collection;

public interface SaveOrUpdateBasicMapper extends BaseBasicMapper {

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @return 影响条数
     */
    default <T> int saveOrUpdate(T entity) {
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
    default <T> int saveOrUpdate(T entity, boolean allFieldForce) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), Tables.get(entity.getClass()), entity, allFieldForce);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @param forceFields 强制字段
     * @return 影响条数
     */
    default <T> int saveOrUpdate(T entity, Getter<T>... forceFields) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), Tables.get(entity.getClass()), entity, false, forceFields);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    default <T> int saveOrUpdate(Collection<T> list) {
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
    default <T> int saveOrUpdate(Collection<T> list, boolean allFieldForce) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), Tables.get(first.getClass()), list, allFieldForce, (Getter<T>[]) null);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list        实体类对象List
     * @param forceFields 强制字段
     * @return 影响条数
     */
    default <T> int saveOrUpdate(Collection<T> list, Getter<T>... forceFields) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), Tables.get(first.getClass()), list, false, forceFields);
    }
}
