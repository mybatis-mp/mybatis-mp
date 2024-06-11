package cn.mybatis.mp.core.sql.executor;


/**
 * 子查询
 */
public class WithQuery extends BaseWithQuery<WithQuery> {

    public WithQuery(String name) {
        super(name);
    }

    public static WithQuery create(String name) {
        return new WithQuery(name);
    }

}
