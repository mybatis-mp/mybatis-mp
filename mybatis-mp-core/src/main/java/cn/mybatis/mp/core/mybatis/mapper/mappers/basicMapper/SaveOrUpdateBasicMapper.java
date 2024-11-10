package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;


import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveOrUpdateMethodUtil;

import java.util.Collection;

public interface SaveOrUpdateBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 实体类新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param entity
     * @return
     */
    default <T> int saveOrUpdate(T entity) {
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
    default <T> int saveOrUpdate(T entity, boolean allFieldForce) {
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), Tables.get(entity.getClass()), entity, allFieldForce);
    }

    default <T> int saveOrUpdate(Collection<T> list) {
        return saveOrUpdate(list, false);
    }

    default <T> int saveOrUpdate(Collection<T> list, boolean allFieldForce) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return SaveOrUpdateMethodUtil.saveOrUpdate(getBasicMapper(), Tables.get(first.getClass()), list, allFieldForce);
    }
}
