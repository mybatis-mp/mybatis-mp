package cn.mybatis.mp.core.sql.executor;

import db.sql.api.impl.cmd.struct.Where;

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
        return new Query(where);
    }

    public <R> Query<R> returnType(Class<R> returnType) {
        return (Query) super.setReturnType(returnType);
    }
}
