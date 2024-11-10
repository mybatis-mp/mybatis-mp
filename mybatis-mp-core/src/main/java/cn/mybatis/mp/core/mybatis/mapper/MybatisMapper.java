package cn.mybatis.mp.core.mybatis.mapper;

import cn.mybatis.mp.core.mybatis.mapper.mappers.*;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.executor.SelectorCall;

import java.util.function.Consumer;

/**
 * 数据库 Mapper
 * $ 开头的方法一般不建议去使用
 *
 * @param <T>
 */
public interface MybatisMapper<T> extends GetMapper<T>, ExistsMapper<T>, CountMapper<T>, ListMapper<T>, CursorMapper<T>,
        PagingMapper<T>, MapWithKeyMapper<T>, SaveMapper<T>, SaveOrUpdateMapper<T>, SaveModelMapper<T>, SaveOrUpdateModelMapper<T>,
        UpdateMapper<T>, UpdateModelMapper<T>, DeleteMapper<T> {

    default DbType getCurrentDbType() {
        return this.getBasicMapper().getCurrentDbType();
    }

    @Override
    default <R> R dbAdapt(Consumer<SelectorCall<R>> consumer) {
        return this.getBasicMapper().dbAdapt(consumer);
    }
}