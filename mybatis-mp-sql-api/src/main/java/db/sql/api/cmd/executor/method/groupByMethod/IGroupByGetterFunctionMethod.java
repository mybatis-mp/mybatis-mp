package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IGroupByGetterFunctionMethod<SELF extends IGroupByGetterFunctionMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> extends IGroupByGetterFunMethod<SELF, TABLE, TABLE_FIELD> {

    default <T> SELF groupBy(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.groupBy(column, 1, f);
    }

    default <T> SELF groupBy(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        return this.groupByWithFun(column, storey, f);
    }

    default <T> SELF groupBy(boolean when, Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(column, 1, f);
    }

    default <T> SELF groupBy(boolean when, Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(column, storey, f);
    }


}
