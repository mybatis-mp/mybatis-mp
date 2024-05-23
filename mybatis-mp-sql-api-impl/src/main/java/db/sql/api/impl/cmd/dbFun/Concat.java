package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.BasicValue;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.io.Serializable;

import static db.sql.api.impl.tookit.SqlConst.CONCAT;
import static db.sql.api.impl.tookit.SqlConst.CONCAT_SPLIT_SYMBOL;

public class Concat extends BasicFunction<Concat> {

    private final Cmd[] values;

    public Concat(Cmd key, Serializable... values) {
        super(CONCAT, key);
        Cmd[] vs = new Cmd[values.length];
        int i = 0;
        for (Serializable value : values) {
            if (value == null) {
                continue;
            }
            vs[i++] = Methods.convert(value);
        }
        this.values = vs;
    }

    public Concat(Cmd key, Cmd... values) {
        super(CONCAT, key);
        this.values = values;
    }

    public Concat(Cmd key, Object... values) {
        super(CONCAT, key);
        Cmd[] vs = new Cmd[values.length];
        int i = 0;
        for (Object value : values) {
            if (value == null) {
                continue;
            }
            if (value instanceof Cmd) {
                vs[i++] = (Cmd) value;
            } else {
                vs[i++] = Methods.convert(value);
            }
        }
        this.values = vs;
    }

    private static StringBuilder join(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder builder, Cmd[] cmds, char[] delimiter) {
        if (cmds == null || cmds.length < 1) {
            return builder;
        }
        int length = cmds.length;
        for (int i = 0; i < length; i++) {
            if (i != 0 && delimiter != null) {
                builder.append(delimiter);
            }
            Cmd value = cmds[i];
            builder = cmds[i].sql(module, parent, context, builder);

            if (value.getClass() == BasicValue.class && context.getDbType() == DbType.PGSQL) {
                BasicValue basicValue = (BasicValue) value;
                if (Objects.nonNull(basicValue)) {
                    builder.append(SqlConst.CAST_TEXT);
                }
            }
        }
        return builder;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if ((context.getDbType() == DbType.ORACLE || context.getDbType() == DbType.DB2) && this.values.length > 1) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.CONCAT_SPLIT_SYMBOL);
            CmdUtils.join(module, parent, context, sqlBuilder, this.values, CONCAT_SPLIT_SYMBOL);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }

        sqlBuilder.append(this.operator).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        join(module, this, context, sqlBuilder, this.values, SqlConst.DELIMITER);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }


    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.key, this.values);
    }
}
