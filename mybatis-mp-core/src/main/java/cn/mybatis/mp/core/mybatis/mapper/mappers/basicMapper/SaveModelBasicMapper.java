package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveModelMethodUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;

import java.util.Collection;
import java.util.Objects;

public interface SaveModelBasicMapper extends BaseMapper, BaseBasicMapper {
    /**
     * 实体类新增
     *
     * @param model
     * @return 影响条数
     */
    default <T, M extends Model<T>> int save(M model) {
        return save(model, false);
    }

    /**
     * 实体类新增
     *
     * @param model         实体类Model实例
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 影响条数
     */
    default <T, M extends Model<T>> int save(M model, boolean allFieldForce) {
        return SaveModelMethodUtil.save(getBasicMapper(), model, allFieldForce);
    }

    /**
     * 实体类新增
     *
     * @param model      实体类Model实例
     * @param saveFields 指定那些列强制插入，null值将会以NULL的形式插入
     * @return 影响条数
     */
    default <T, M extends Model<T>> int save(M model, Getter<M>... saveFields) {
        return SaveModelMethodUtil.save(getBasicMapper(), model, saveFields);
    }


    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @return 插入条数
     */
    default <T, M extends Model<T>> int saveModel(Collection<M> list) {
        return saveModel(list, false);
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 插入条数
     */
    default <T, M extends Model<T>> int saveModel(Collection<M> list, boolean allFieldForce) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        return SaveModelMethodUtil.save(getBasicMapper(), list, allFieldForce);
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     *
     * @param list
     * @return 插入的条数
     */
    default <T, M extends Model<T>> int saveModelBatch(Collection<M> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        return SaveModelMethodUtil.saveBatch(getBasicMapper(), list);
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
    default <T, M extends Model<T>> int saveModelBatch(Collection<M> list, Getter<M>... saveFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        return SaveModelMethodUtil.saveBatch(getBasicMapper(), list, saveFields);
    }
}