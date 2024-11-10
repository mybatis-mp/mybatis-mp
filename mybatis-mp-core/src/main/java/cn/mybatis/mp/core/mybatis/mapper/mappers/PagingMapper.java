package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.PagingMethodUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public interface PagingMapper<T> extends BaseMapper, BaseMybatisMapper<T> {

    /**
     * 分页查询
     *
     * @param consumer where consumer
     * @param pager    分页参数
     * @return 分页结果
     */
    default <P extends Pager<T>> P paging(P pager, Consumer<Where> consumer) {
        return this.paging(pager, consumer, null);
    }

    /**
     * 分页查询
     *
     * @param consumer     where consumer
     * @param pager        pager
     * @param selectFields select指定列
     * @return
     */
    default <P extends Pager<T>> P paging(P pager, Consumer<Where> consumer, Getter<T>... selectFields) {
        return PagingMethodUtil.paging(getBasicMapper(), getTableInfo(), pager, consumer, selectFields);
    }

    /**
     * 分页查询
     *
     * @param where where
     * @param pager 分页参数
     * @return 分页结果
     */
    default <P extends Pager<T>> P paging(P pager, Where where) {
        return this.paging(pager, where, null);
    }

    default <P extends Pager<T>> P paging(P pager, Where where, Getter<T>... selectFields) {
        return PagingMethodUtil.paging(getBasicMapper(), getTableInfo(), pager, where, selectFields);
    }
}
