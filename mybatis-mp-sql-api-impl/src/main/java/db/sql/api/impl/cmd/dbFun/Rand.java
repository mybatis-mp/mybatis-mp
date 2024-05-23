package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

import java.util.Objects;

public class Rand extends BasicFunction<Rand> {

    private final Number max;

    public Rand(Cmd value) {
        this(value, null);
    }

    public Rand(Cmd value, Number max) {
        super(SqlConst.RAND, value);
        this.max = max;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        if (Objects.nonNull(this.max)) {
            sqlBuilder.append(SqlConst.DELIMITER).append(this.max);
        }
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}