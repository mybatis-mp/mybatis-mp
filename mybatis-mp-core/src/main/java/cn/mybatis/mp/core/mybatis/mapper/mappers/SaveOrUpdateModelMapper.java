package cn.mybatis.mp.core.mybatis.mapper.mappers;


import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveOrUpdateModelMethodUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;

import java.util.Collection;

public interface SaveOrUpdateModelMapper<T> extends BaseMapper<T> {

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param model
     * @param <M>
     * @return 影响条数
     */
    default <M extends Model<T>> int saveOrUpdate(M model) {
        return this.saveOrUpdate(model, false);
    }

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param model
     * @param allFieldForce 所有字段都强制保存
     * @param <M>
     * @return 影响条数
     */
    default <M extends Model<T>> int saveOrUpdate(M model, boolean allFieldForce) {
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), Models.get(model.getClass()), model, allFieldForce, null);
    }

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param model
     * @param forceFields 强制字段
     * @param <M>
     * @return 影响条数
     */
    default <M extends Model<T>> int saveOrUpdate(M model, Getter<M>... forceFields) {
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), Models.get(model.getClass()), model, false, forceFields);
    }

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list 实体类Model 对象List
     * @return 影响条数
     */
    default <M extends Model<T>> int saveOrUpdateModel(Collection<M> list) {
        return this.saveOrUpdateModel(list, false);
    }

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list          实体类Model 对象List
     * @param allFieldForce 是否所有字段强制
     * @return 影响条数
     */
    default <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, boolean allFieldForce) {
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), list, allFieldForce, null);
    }

    /**
     * 实体类Model新增或修改
     * 先查是否存在，再进行新增或修改
     *
     * @param list        实体类Model 对象List
     * @param forceFields 强制字段
     * @return 影响条数
     */
    default <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, Getter<M>... forceFields) {
        return SaveOrUpdateModelMethodUtil.saveOrUpdate(getBasicMapper(), list, false, forceFields);
    }
}
