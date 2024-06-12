package db.sql.api.cmd.executor.method;

import db.sql.api.Cmd;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.executor.method.groupByMethod.IGroupByMethods;

public interface IGroupByMethod<SELF extends IGroupByMethod,
        TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd>

        extends IGroupByMethods<SELF,
        TABLE,
        TABLE_FIELD,
        COLUMN> {

}