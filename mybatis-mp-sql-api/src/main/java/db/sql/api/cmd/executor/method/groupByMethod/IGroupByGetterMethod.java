package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IGroupByGetterMethod<SELF extends IGroupByGetterMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    default <T> SELF groupBy(Getter<T> column) {
        return this.groupBy(column, 1);
    }

    <T> SELF groupBy(Getter<T> column, int storey);

    default <T> SELF groupBy(boolean when, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(column, 1);
    }

    default <T> SELF groupBy(boolean when, Getter<T> column, int storey) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(column, storey);
    }

    //---

    default <T> SELF groupBy(Getter<T> column, Function<TABLE_FIELD, Cmd> f) {
        return this.groupBy(column, 1, f);
    }

    <T> SELF groupBy(Getter<T> column, int storey, Function<TABLE_FIELD, Cmd> f);

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
