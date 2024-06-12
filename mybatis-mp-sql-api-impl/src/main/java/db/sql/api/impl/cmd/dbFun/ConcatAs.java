package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.BasicValue;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.io.Serializable;

import static db.sql.api.impl.tookit.SqlConst.CONCAT;
import static db.sql.api.impl.tookit.SqlConst.CONCAT_WS;

public class ConcatAs extends BasicFunction<ConcatAs> {

    private final Cmd[] values;

    private final Cmd split;

    public ConcatAs(Cmd key, String split, Serializable... values) {
        super(CONCAT_WS, key);
        Cmd[] vs = new Cmd[values.length];
        int i = 0;
        for (Serializable value : values) {
            if (value == null) {
                continue;
            }
            vs[i++] = Methods.convert(value);
        }
        this.split = Methods.convert(split);
        this.values = vs;
    }

    public ConcatAs(Cmd key, String split, Cmd... values) {
        super(CONCAT_WS, key);
        this.split = Methods.convert(split);
        this.values = values;
    }

    public ConcatAs(Cmd key, String split, Object... values) {
        super(CONCAT, key);
        this.split = Methods.convert(split);
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
                    builder.append(SqlConst.CAST_TEXT);

            }
        }
        return builder;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (this.values == null || this.values.length < 1) {
            return sqlBuilder;
        }
        if (context.getDbType() == DbType.ORACLE || context.getDbType() == DbType.DB2) {
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            this.key.sql(module, parent, context, sqlBuilder);
            for (int i = 0; i < this.values.length; i++) {

                sqlBuilder.append(SqlConst.CONCAT_SPLIT_SYMBOL);
                this.split.sql(module, parent, context, sqlBuilder);
                sqlBuilder.append(SqlConst.CONCAT_SPLIT_SYMBOL);


                this.values[i].sql(module, parent, context, sqlBuilder);

            }
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }
        sqlBuilder.append(this.operator).append(SqlConst.BRACKET_LEFT);
        this.split.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
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
