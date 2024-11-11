package cn.mybatis.mp.core.mybatis.mapper.mappers;


import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveOrUpdateMethodUtil;

import java.util.Collection;

public interface SaveOrUpdateMapper<T> extends BaseMapper<T> {

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @return
     */
    default int saveOrUpdate(T entity) {
        return saveOrUpdate(entity, false);
    }

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @param @allFieldForce 所有字段都强制保存
     * @return
     */
    default int saveOrUpdate(T entity, boolean allFieldForce) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), entity, allFieldForce);
    }

    default int saveOrUpdate(Collection<T> list) {
        return saveOrUpdate(list, false);
    }

    default int saveOrUpdate(Collection<T> list, boolean allFieldForce) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), getTableInfo(), list, allFieldForce);
    }
}
