package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.struct.query.Select;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.impl.tookit.SqlUtil;

public interface Function<T> extends Cmd {

    String getAlias();

    T as(String alias);

    default <T2> T as(Getter<T2> aliasGetter) {
        return this.as(SqlUtil.getAsName(aliasGetter));
    }

    /**
     * 拼接别名
     *
     * @param module
     * @param user
     * @param context
     * @param sqlBuilder
     */
    default void appendAlias(Cmd module, Cmd user, SqlBuilderContext context, StringBuilder sqlBuilder) {
        //拼接 select 的别名
        if (module instanceof Select && user instanceof Select) {
            if (this.getAlias() != null) {
                sqlBuilder.append(SqlConst.AS(context.getDbType()));
                sqlBuilder.append(this.getAlias());
            }

        }
    }

    /**
     * 函数的sql
     *
     * @param module
     * @param parent
     * @param context
     * @param sqlBuilder
     * @return
     */
    StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder);

    @Override
    default StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        this.functionSql(module, parent, context, sqlBuilder);
        this.appendAlias(module, parent, context, sqlBuilder);
        return sqlBuilder;
    }
}
