package db.sql.api.cmd.executor.method;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.executor.method.orderByMethod.IOrderByMethods;

public interface IOrderByMethod<SELF extends IOrderByMethod,
        TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd>

        extends IOrderByMethods<SELF,
        TABLE,
        TABLE_FIELD,
        COLUMN> {


}
