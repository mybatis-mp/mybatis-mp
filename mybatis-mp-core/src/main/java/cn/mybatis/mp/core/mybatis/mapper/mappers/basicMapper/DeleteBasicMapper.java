package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.DeleteMethodUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

public interface DeleteBasicMapper extends BaseMapper, BaseBasicMapper {
    /**
     * 根据实体类删除
     *
     * @param entity 实体类实例
     * @return 影响的数量
     */
    default <T> int delete(T entity) {
        return DeleteMethodUtil.delete(getBasicMapper(), Tables.get(entity.getClass()), entity);
    }

    /**
     * 多个删除
     *
     * @param list 实体类实例list
     * @return 修改条数
     */
    default <T> int delete(Collection<T> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        T first = list.stream().findFirst().get();
        return DeleteMethodUtil.delete(getBasicMapper(), Tables.get(first.getClass()), list);
    }


    /**
     * 根据id删除
     * @param entityType 实体类
     * @param id ID
     * @return 影响的数量
     */
    default <T, ID extends Serializable> int deleteById(Class<T> entityType, ID id) {
        return DeleteMethodUtil.deleteById(getBasicMapper(), Tables.get(entityType), id);
    }

    /**
     * 批量删除多个
     * @param entityType 实体类
     * @param ids 多个ID
     * @return 影响的数量
     */
    default <T, ID extends Serializable> int deleteByIds(Class<T> entityType, ID... ids) {
        return DeleteMethodUtil.deleteByIds(getBasicMapper(), Tables.get(entityType), ids);
    }

    /**
     * 批量删除多个
     * @param entityType 实体类
     * @param ids 多个ID
     * @return 影响数量
     */
    default <T, ID extends Serializable> int deleteByIds(Class<T> entityType, Collection<ID> ids) {
        return DeleteMethodUtil.deleteByIds(getBasicMapper(), Tables.get(entityType), ids);
    }

    /**
     * 动态条件删除
     * @param entityType 实体类
     * @param consumer
     * @return
     */
    default <T> int delete(Class<T> entityType, Consumer<Where> consumer) {
        return DeleteMethodUtil.delete(getBasicMapper(), Tables.get(entityType), consumer);
    }

    /**
     * 动态条件删除
     * @param entityType 实体类
     * @param where
     * @return
     */
    default <T> int delete(Class<T> entityType, Where where) {
        return DeleteMethodUtil.delete(getBasicMapper(), Tables.get(entityType), where);
    }

    /**
     * 删除所有数据
     * @param entityType 实体类
     * @return
     */
    default <T> int deleteAll(Class<T> entityType) {
        return DeleteMethodUtil.deleteAll(getBasicMapper(), Tables.get(entityType));
    }
}
