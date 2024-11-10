package cn.mybatis.mp.core.sql.executor;

import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public class Query<T> extends BaseQuery<Query<T>, T> {

    public Query() {
        super();
    }

    public Query(Where where) {
        super(where);
    }

    public static <T> Query<T> create() {
        return new Query();
    }

    public static <T> Query<T> create(Where where) {
        if (where == null) {
            return create();
        }
        return new Query(where);
    }

    public <R> Query<R> returnType(Class<R> returnType) {
        return (Query<R>) super.setReturnType(returnType);
    }

    public <R> Query<R> returnType(Class<R> returnType, Consumer<R> consumer) {
        return (Query<R>) super.setReturnType(returnType, consumer);
    }
}
