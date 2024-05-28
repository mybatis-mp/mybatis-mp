package db.sql.api.impl.cmd.executor;

import db.sql.api.cmd.executor.IExecutor;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.basic.DatasetField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;

public interface Executor<SELF extends Executor,
        CMD_FACTORY extends CmdFactory
        >
        extends IExecutor<SELF, Table, Dataset, TableField, DatasetField> {

    CMD_FACTORY $();
}
