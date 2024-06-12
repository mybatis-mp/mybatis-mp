package db.sql.api.cmd.executor.method;

import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.executor.method.havingMethod.IHavingMethods;
import db.sql.api.cmd.struct.query.IHaving;

import java.util.function.Consumer;

public interface IHavingMethod<SELF extends IHavingMethod,
        TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        HAVING extends IHaving<HAVING>
        >
        extends IHavingMethods<SELF, TABLE, TABLE_FIELD> {

    SELF having(Consumer<HAVING> consumer);

}
