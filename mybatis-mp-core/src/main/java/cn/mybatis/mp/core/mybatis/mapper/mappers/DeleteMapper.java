package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.DeleteMethodUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Collection;
import java.util.function.Consumer;

public interface DeleteMapper<T> extends BaseMapper, BaseMybatisMapper<T> {
    /**
     * 根据实体类删除
     *
     * @param entity 实体类实例
     * @return 影响的数量
     */
    default int delete(T entity) {
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), entity);
    }

    /**
     * 多个删除
     *
     * @param list 实体类实例list
     * @return 修改条数
     */
    default int delete(Collection<T> list) {
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), list);
    }


    /**
     * 根据id删除
     *
     * @param id ID
     * @return 影响的数量
     */
    default <ID extends Serializable> int deleteById(ID id) {
        return DeleteMethodUtil.deleteById(getBasicMapper(), getTableInfo(), id);
    }

    /**
     * 批量删除多个
     *
     * @param ids 多个ID
     * @return 影响的数量
     */
    default <ID extends Serializable> int deleteByIds(ID... ids) {
        return DeleteMethodUtil.deleteByIds(getBasicMapper(), getTableInfo(), ids);
    }

    /**
     * 批量删除多个
     *
     * @param ids 多个ID
     * @return 影响数量
     */
    default <ID extends Serializable> int deleteByIds(Collection<ID> ids) {
        return DeleteMethodUtil.deleteByIds(getBasicMapper(), getTableInfo(), ids);
    }

    /**
     * 动态条件删除
     *
     * @param consumer
     * @return
     */
    default int delete(Consumer<Where> consumer) {
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), consumer);
    }

    /**
     * 动态条件删除
     *
     * @param where
     * @return
     */
    default int delete(Where where) {
        return DeleteMethodUtil.delete(getBasicMapper(), getTableInfo(), where);
    }

    /**
     * 删除所有数据
     *
     * @return
     */
    default int deleteAll() {
        return DeleteMethodUtil.deleteAll(getBasicMapper(), getTableInfo());
    }
}
