package db.sql.api.impl.cmd.struct;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.executor.AbstractUpdate;

public class FromTable extends From<Dataset> {

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (module instanceof AbstractUpdate) {
            //MYSQL MARIA_DB 不支持 update from
            if (context.getDbType() == DbType.MYSQL || context.getDbType() == DbType.MARIA_DB) {
                return sqlBuilder;
            }
        }
        return super.sql(module, parent, context, sqlBuilder);
    }
}

