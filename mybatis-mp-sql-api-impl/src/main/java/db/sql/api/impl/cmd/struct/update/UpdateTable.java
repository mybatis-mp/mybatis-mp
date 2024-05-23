package db.sql.api.impl.cmd.struct.update;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.struct.update.IUpdateTable;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.executor.AbstractUpdate;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.Objects;

public class UpdateTable implements IUpdateTable<Table> {

    private final Table[] tables;

    public UpdateTable(Table[] tables) {
        this.tables = tables;
    }

    @Override
    public Table[] getTables() {
        return tables;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (this.tables == null || this.tables.length < 1) {
            return sqlBuilder;
        }

        sqlBuilder.append(SqlConst.UPDATE);
        int length = this.tables.length;
        for (int i = 0; i < length; i++) {
            Table table = this.tables[i];
            if (i != 0) {
                sqlBuilder.append(SqlConst.DELIMITER);
            }

            if (context.getDbType() == DbType.PGSQL) {
                //只能修改一张
                AbstractUpdate abstractUpdate = (AbstractUpdate) module;
                if (Objects.nonNull(abstractUpdate.getFrom())) {
                    abstractUpdate.getFrom().getTables().remove(table);
                }
            }

            if (context.getDbType() == DbType.SQL_SERVER) {
                //SQL_SERVER 别名支持的话 需要 带有from ；否则 只能是表面
                AbstractUpdate abstractUpdate = (AbstractUpdate) module;
                if (Objects.nonNull(abstractUpdate.getFrom())) {
                    if (table.getAlias() != null) {
                        sqlBuilder.append(table.getAlias());
                    } else {
                        sqlBuilder.append(table.getName());
                    }
                    sqlBuilder.append(SqlConst.BLANK);
                    return sqlBuilder;
                } else {
                    //没有 from 不加别名
                    table.setAlias(null);
                    sqlBuilder.append(table.getName());
                    sqlBuilder.append(SqlConst.BLANK);
                    return sqlBuilder;
                }
            }

            sqlBuilder.append(table.getName());
            sqlBuilder.append(SqlConst.BLANK);
            if (table.getAlias() != null) {
                sqlBuilder.append(table.getAlias());
            }
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, (Object[]) this.tables);
    }
}
