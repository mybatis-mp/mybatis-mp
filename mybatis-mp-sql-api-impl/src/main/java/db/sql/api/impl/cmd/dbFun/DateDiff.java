package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.basic.BasicValue;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class DateDiff extends BasicFunction<DateDiff> {

    private final Cmd another;

    public DateDiff(Cmd key, Cmd another) {
        super(SqlConst.DATE_DIFF, key);
        this.another = another;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if(context.getDbType() == DbType.H2 || context.getDbType() == DbType.SQL_SERVER || context.getDbType() == DbType.DM){
            sqlBuilder.append("ABS(").append(operator).append(SqlConst.BRACKET_LEFT);
            sqlBuilder.append("DAY,");
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.DELIMITER);
            this.another.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT).append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        } else if(context.getDbType() == DbType.PGSQL  ){
            sqlBuilder.append(SqlConst.BRACKET_LEFT).append("DATE_PART").append(SqlConst.BRACKET_LEFT);
            sqlBuilder.append("'DAY',");
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            sqlBuilder.append('-');
            sqlBuilder.append("DATE_PART").append(SqlConst.BRACKET_LEFT);
            sqlBuilder.append("'DAY',");
            this.another.sql(module, this, context, sqlBuilder).append("::DATE ");
            sqlBuilder.append(SqlConst.BRACKET_RIGHT).append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }else if(context.getDbType() == DbType.DB2){
            sqlBuilder.append(SqlConst.BRACKET_LEFT);
            sqlBuilder.append("DAYS").append(SqlConst.BRACKET_LEFT);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            sqlBuilder.append('-');
            sqlBuilder.append("DAYS").append(SqlConst.BRACKET_LEFT);

            if(this.another instanceof BasicValue){
                sqlBuilder.append("CAST(");
            }
            this.another.sql(module, this, context, sqlBuilder);
            if(this.another instanceof BasicValue){
                sqlBuilder.append(" AS TIMESTAMP )");
            }

            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }else if(context.getDbType() == DbType.MYSQL || context.getDbType() == DbType.MARIA_DB){
            sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
            this.key.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.DELIMITER);
            this.another.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            return sqlBuilder;
        }
        sqlBuilder.append("MP_DATE_DIFF").append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        this.another.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.key, this.another);
    }
}
