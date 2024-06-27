package cn.mybatis.mp.core.sql.executor;

import db.sql.api.impl.cmd.struct.Where;

/**
 * 子查询
 */
public class SubQuery extends BaseSubQuery<SubQuery> {

    public SubQuery() {
        this((String) null);
    }

    public SubQuery(String alias) {
        super(alias);
    }

    public SubQuery(Where where) {
        super(null, where);
    }

    public SubQuery(String alias, Where where) {
        super(alias, where);
    }

    public static SubQuery create() {
        return new SubQuery();
    }

    public static SubQuery create(Where where) {
        return new SubQuery(where);
    }

    public static SubQuery create(String alias) {
        return new SubQuery(alias);
    }

    public static SubQuery create(String alias, Where where) {
        return new SubQuery(alias, where);
    }
}
