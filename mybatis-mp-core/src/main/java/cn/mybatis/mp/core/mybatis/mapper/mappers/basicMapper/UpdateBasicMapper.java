package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.UpdateMethodUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Collection;
import java.util.function.Consumer;

public interface UpdateBasicMapper extends BaseMapper, BaseBasicMapper {

    /**
     * 实体类修改
     *
     * @param entity 实体类对象
     * @return 影响条数
     */
    default <T> int update(T entity) {
        return this.update(entity, false);
    }

    /**
     * 实体类修改
     *
     * @param entity 实体类对象
     * @return 影响条数
     * @allFieldForce 所有字段都强制保存
     */
    default <T> int update(T entity, boolean allFieldForce) {
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(entity.getClass()), entity, allFieldForce, (Getter<T>[]) null);
    }

    /**
     * 实体类修改
     *
     * @param entity
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 返回修改条数
     */
    default <T> int update(T entity, Getter<T>... forceUpdateFields) {
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(entity.getClass()), entity, false, forceUpdateFields);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    default <T> int update(Collection<T> list) {
        return this.update(list, false);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list 实体类对象List
     * @return 影响条数
     * @allFieldForce 所有字段都强制保存
     */
    default <T> int update(Collection<T> list, boolean allFieldForce) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(first.getClass()), list, allFieldForce, (Getter<T>[]) null);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list              实体类对象List
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return 修改条数
     */
    default <T> int update(Collection<T> list, Getter<T>... forceUpdateFields) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(first.getClass()), list, false, forceUpdateFields);
    }


    /**
     * 动态条件修改
     *
     * @param entity   实体类
     * @param consumer where
     * @return
     */
    default <T> int update(T entity, Consumer<Where> consumer) {
        return this.update(entity, false, consumer);
    }

    /**
     * 动态条件修改
     *
     * @param entity        实体类对象
     * @param allFieldForce 所有字段都强制保存
     * @param consumer      where
     * @return
     */
    default <T> int update(T entity, boolean allFieldForce, Consumer<Where> consumer) {
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(entity.getClass()), entity, allFieldForce, consumer);
    }

    /**
     * 动态where 修改
     *
     * @param entity            实体类对象
     * @param consumer          where
     * @param forceUpdateFields 强制更新指定，解决需要修改为null的需求
     * @return
     */
    default <T> int update(T entity, Consumer<Where> consumer, Getter<T>... forceUpdateFields) {
        return UpdateMethodUtil.update(getBasicMapper(), Tables.get(entity.getClass()), entity, consumer, forceUpdateFields);
    }
}
