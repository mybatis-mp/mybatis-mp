package db.sql.api.impl.cmd.condition;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.LikeMode;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.BasicValue;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;

public class Like extends BasicCondition {
    private final LikeMode mode;

    public Like(char[] operator, LikeMode mode, Cmd key, Cmd value) {
        super(operator, key, value);
        this.mode = mode;
    }

    public Like(Cmd key, Cmd value) {
        this(LikeMode.DEFAULT, key, value);
    }

    public Like(Cmd key, Object value) {
        this(key, Methods.cmd(value));
    }

    public Like(LikeMode mode, Cmd key, Cmd value) {
        this(SqlConst.LIKE, mode, key, value);
    }

    public Like(LikeMode mode, Cmd key, Object value) {
        this(SqlConst.LIKE, mode, key, Methods.cmd(value));
    }

    public LikeMode getMode() {
        return mode;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        getField().sql(module, this, context, sqlBuilder);
        sqlBuilder.append(getOperator());


        boolean before = false;
        boolean after = false;

        switch (this.mode) {
            case NONE: {
                break;
            }
            case DEFAULT: {
                before = true;
                after = true;
                break;
            }
            case RIGHT: {
                after = true;
                break;
            }
            default: {
                before = true;
            }
        }


        if (context.getDbType() == DbType.DB2) {
            //由于 DB2 CONCAT 不支持 变量，所以只能换方式
            if (!before && !after) {
                getValue().sql(module, this, context, sqlBuilder);
            } else {
                sqlBuilder.append(SqlConst.BRACKET_LEFT);
                if (before) {
                    sqlBuilder.append(SqlConst.VAGUE_SYMBOL);
                    sqlBuilder.append(SqlConst.CONCAT_SPLIT_SYMBOL);
                }
                getValue().sql(module, this, context, sqlBuilder);
                if (after) {
                    sqlBuilder.append(SqlConst.CONCAT_SPLIT_SYMBOL).append(SqlConst.VAGUE_SYMBOL);
                }
                sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            }
            return sqlBuilder;
        }

        if (before && after && context.getDbType() == DbType.ORACLE) {
            //ORACLE Concat 不支持2个以上参数
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            sqlBuilder.append(SqlConst.VAGUE_SYMBOL).append(SqlConst.CONCAT_SPLIT_SYMBOL);
            getValue().sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.CONCAT_SPLIT_SYMBOL).append(SqlConst.VAGUE_SYMBOL);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }

        if (before || after) {
            sqlBuilder.append(SqlConst.CONCAT).append(SqlConst.BRACKET_LEFT);
        }


        if (before) {
            sqlBuilder.append(SqlConst.VAGUE_SYMBOL).append(SqlConst.DELIMITER);
        }
        getValue().sql(module, this, context, sqlBuilder);

        if (getValue().getClass() == BasicValue.class && context.getDbType() == DbType.PGSQL) {
            BasicValue basicValue = (BasicValue) getValue();
            if (Objects.nonNull(basicValue)) {
                sqlBuilder.append(SqlConst.CAST_TEXT);
            }
        }

        if (after) {
            sqlBuilder.append(SqlConst.DELIMITER).append(SqlConst.VAGUE_SYMBOL);
        }
        if (before || after) {
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        }
        return sqlBuilder;
    }
}
