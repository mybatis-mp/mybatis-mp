package db.sql.api.impl.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IOrderByDirection;

public enum OrderByDirection implements IOrderByDirection {

    NONE("", ""),

    ASC(" ASC", ""),
    ASC_NULLS_FIRST(" ASC", " NULLS FIRST"),
    ASC_NULLS_LAST(" ASC", " NULLS LAST"),
    DESC(" DESC", ""),
    DESC_NULLS_FIRST(" DESC", " NULLS FIRST"),
    DESC_NULLS_LAST(" DESC", " NULLS LAST");
    private final String directionSQL;

    private final String nullsDirectionSQL;

    OrderByDirection(String directionSQL, String nullsDirectionSQL) {
        this.directionSQL = directionSQL;
        this.nullsDirectionSQL = nullsDirectionSQL;
    }


    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(this.directionSQL);
        if (!IOrderByDirection.isSupportNullsOrder(context.getDbType())) {
            return sqlBuilder;
        } else {
            sqlBuilder.append(this.nullsDirectionSQL);
        }
        return sqlBuilder;
    }

}
