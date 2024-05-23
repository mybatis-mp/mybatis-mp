package cn.mybatis.mp.core.sql.executor;

import db.sql.api.impl.cmd.struct.Where;

public class Query extends BaseQuery<Query> {

    public Query() {
        super();
    }

    public Query(Where where) {
        super(where);
    }

    public static Query create() {
        return new Query();
    }

    public static Query create(Where where) {
        return new Query(where);
    }
}
