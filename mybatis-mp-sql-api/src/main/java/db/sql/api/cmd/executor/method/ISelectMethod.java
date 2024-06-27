package db.sql.api.cmd.executor.method;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.*;
import db.sql.api.cmd.executor.method.selectMethod.ISelectMethods;

public interface ISelectMethod<SELF extends ISelectMethod,
        TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd>

        extends ISelectMethods<SELF,
        TABLE,
        TABLE_FIELD,
        COLUMN> {

    SELF selectDistinct();

    default SELF select1() {
        this.select((COLUMN) SQL1.INSTANCE);
        return (SELF) this;
    }

    default SELF selectAll() {
        this.select((COLUMN) SQLCmdAll.INSTANCE);
        return (SELF) this;
    }

    default SELF selectAll(IDataset<?, ?> dataset) {
        this.select((COLUMN) new SQLCmdAll(dataset));
        return (SELF) this;
    }

    SELF selectCount1();

    SELF selectCountAll();

    default SELF select(Class entity) {
        return this.select(entity, 1);
    }

    SELF select(Class entity, int storey);

    default SELF select(Class... entities) {
        return this.select(1, entities);
    }

    default SELF select(int storey, Class... entities) {
        for (Class entity : entities) {
            this.select(entity, storey);
        }
        return (SELF) this;
    }

    default <T> SELF selectIgnore(Getter<T> column) {
        return this.selectIgnore(column, 1);
    }

    <T> SELF selectIgnore(Getter<T> column, int storey);

    default <T> SELF selectIgnore(Getter<T>... columns) {
        return this.selectIgnore(1, columns);
    }

    default <T> SELF selectIgnore(int storey, Getter<T>... columns) {
        for (Getter column : columns) {
            this.selectIgnore(column, storey);
        }
        return (SELF) this;
    }
}
