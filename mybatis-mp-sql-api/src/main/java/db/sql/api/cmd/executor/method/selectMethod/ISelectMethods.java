package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface ISelectMethods<SELF extends ISelectMethods,
        TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd>
        extends
        ISelectCmdMethod<SELF, COLUMN>,
        ISelectGetterMethod<SELF>,
        ISelectGetterFunMethod<SELF, TABLE, TABLE_FIELD>,
        ISelectMultiGetterMethod<SELF>,
        ISelectMultiGetterFunMethod<SELF, TABLE, TABLE_FIELD>,
        ISelectDatasetMethod<SELF>,
        ISelectDatasetGetterMethod<SELF>,
        ISelectDatasetGetterFunMethod<SELF>,
        ISelectDatasetMultiGetterMethod<SELF>,
        ISelectDatasetMultiGetterFunMethod<SELF> {

    @Override
    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF select(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        for (Getter<T> column : columns) {
            this.select(dataset, column);
        }
        return (SELF) this;
    }

    SELF select(String columnName);

    SELF selectWithFun(String columnName, Function<IDatasetField, Cmd> f);

    default SELF select(boolean when, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(columnName);
    }

    default SELF selectWithFun(boolean when, String columnName, Function<IDatasetField, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(columnName, f);
    }
}
