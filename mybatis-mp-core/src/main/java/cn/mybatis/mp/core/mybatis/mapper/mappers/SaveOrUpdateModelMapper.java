package cn.mybatis.mp.core.mybatis.mapper.mappers;


import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveOrUpdateModelMethodUtil;
import cn.mybatis.mp.db.Model;

import java.util.Collection;

public interface SaveOrUpdateModelMapper<T> extends BaseMapper, BaseMybatisMapper<T> {

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param model
     * @param <M>
     * @return
     */
    default <M extends Model<T>> int saveOrUpdate(M model) {
        return this.saveOrUpdate(model, false);
    }

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param model
     * @param @allFieldForce 所有字段都强制保存
     * @param <M>
     * @return
     */
    default <M extends Model<T>> int saveOrUpdate(M model, boolean allFieldForce) {
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), Models.get(model.getClass()), model, allFieldForce);
    }

    default <M extends Model<T>> int saveOrUpdateModel(Collection<M> list) {
        return this.saveOrUpdateModel(list, false);
    }

    default <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, boolean allFieldForce) {
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), list, allFieldForce);
    }
}
