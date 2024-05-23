package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.IExecutor;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.tookit.CmdUtils;

import java.util.List;

public interface Executor<SELF extends Executor,
        CMD_FACTORY extends CmdFactory
        >
        extends IExecutor<SELF, Table, Dataset, TableField, DatasetField> {

    CMD_FACTORY $();

    @Override
    default StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        return this.sql(context, sqlBuilder);
    }

    @Override
    default StringBuilder sql(SqlBuilderContext context, StringBuilder sqlBuilder) {
        List<Cmd> cmdList = cmds();
        if (cmdList == null || cmdList.isEmpty()) {
            return sqlBuilder;
        }
        cmdList = sortedCmds();
        return CmdUtils.join(this, this, context, sqlBuilder, cmdList);
    }
}
