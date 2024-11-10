package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.UpdateModelMethodUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Collection;
import java.util.function.Consumer;

public interface UpdateModelBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 实体类修改
     *
     * @param model 实体类对象
     * @return 影响条数
     */
    default <T, M extends Model<T>> int update(M model) {
        return this.update(model, false);
    }

    /**
     * 实体类修改
     *
     * @param model 实体类对象
     * @return 影响条数
     * @allFieldForce 所有字段都强制保存
     */
    default <T, M extends Model<T>> int update(M model, boolean allFieldForce) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, allFieldForce, (Getter<M>[]) null);
    }

    /**
     * 实体类修改
     *
     * @param model
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 返回修改条数
     */
    default <T, M extends Model<T>> int update(M model, Getter<M>... forceUpdateFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, false, forceUpdateFields);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    default <T, M extends Model<T>> int updateModel(Collection<M> list) {
        return this.updateModel(list, false);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list 实体类对象List
     * @return 影响条数
     * @allFieldForce 所有字段都强制保存
     */
    default <T, M extends Model<T>> int updateModel(Collection<M> list, boolean allFieldForce) {
        return UpdateModelMethodUtil.update(getBasicMapper(), list, allFieldForce, null);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list              实体类对象List
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 修改条数
     */
    default <T, M extends Model<T>> int updateModel(Collection<M> list, Getter<M>... forceUpdateFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), list, false, forceUpdateFields);
    }


    /**
     * 动态条件修改
     *
     * @param model    实体类
     * @param consumer where
     * @return
     */
    default <T, M extends Model<T>> int update(M model, Consumer<Where> consumer) {
        return this.update(model, false, consumer);
    }

    /**
     * 动态条件修改
     *
     * @param model         实体类对象
     * @param allFieldForce 所有字段都强制保存
     * @param consumer      where
     * @return
     */
    default <T, M extends Model<T>> int update(M model, boolean allFieldForce, Consumer<Where> consumer) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, allFieldForce, consumer);
    }

    /**
     * 动态where 修改
     *
     * @param model             实体类对象
     * @param consumer          where
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return
     */
    default <T, M extends Model<T>> int update(M model, Consumer<Where> consumer, Getter<M>... forceUpdateFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, consumer, forceUpdateFields);
    }
}
