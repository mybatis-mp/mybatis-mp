package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.DatePattern;
import db.sql.api.impl.tookit.SqlConst;

public class DateFormat extends BasicFunction<DateFormat> {

    private final Cmd pattern;

    public DateFormat(Cmd key, String pattern) {
        super(null, key);
        this.pattern = Methods.convert(pattern);
    }

    public DateFormat(Cmd key, DatePattern datePattern) {
        super(null, key);
        this.pattern = datePattern;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.DATE_FORMAT(context.getDbType()));
        sqlBuilder.append(SqlConst.BRACKET_LEFT);

        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        pattern.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}