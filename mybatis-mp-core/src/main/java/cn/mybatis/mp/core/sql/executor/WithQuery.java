package cn.mybatis.mp.core.sql.executor;


/**
 * 子查询
 */
public class WithQuery extends BaseWithQuery<WithQuery> {

    public WithQuery() {
        this(null);
    }

    public WithQuery(String alias) {
        super(alias);
    }

    public static WithQuery create() {
        return new WithQuery(null);
    }

    public static WithQuery create(String alias) {
        return new WithQuery(alias);
    }
}
