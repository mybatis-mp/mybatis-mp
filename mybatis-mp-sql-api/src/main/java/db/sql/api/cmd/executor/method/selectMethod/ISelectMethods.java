package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IColumn;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface ISelectMethods<SELF extends ISelectMethods,
        TABLE_FIELD extends Cmd,
        DATASET_FILED extends Cmd,
        COLUMN extends Cmd>
        extends
        ISelectCmdMethod<SELF, COLUMN>,
        ISelectGetterMethod<SELF>,
        ISelectGetterFunMethod<SELF, TABLE_FIELD>,
        ISelectMultiGetterMethod<SELF>,
        ISelectMultiGetterFunMethod<SELF, TABLE_FIELD>,
        ISelectDatasetMethod<SELF, DATASET_FILED>,
        ISelectDatasetGetterMethod<SELF, DATASET_FILED>,
        ISelectDatasetGetterFunMethod<SELF, DATASET_FILED>,
        ISelectDatasetMultiGetterMethod<SELF>,
        ISelectDatasetMultiGetterFunMethod<SELF, DATASET_FILED> {

    @Override
    default <T> SELF select(IDataset dataset, Getter<T>... columns) {
        for (Getter<T> column : columns) {
            this.select(dataset, column);
        }
        return (SELF) this;
    }

    SELF select(String columnName);

    SELF selectWithFun(String columnName, Function<IColumn, Cmd> f);

    default SELF select(boolean when, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(columnName);
    }

    default SELF selectWithFun(boolean when, String columnName, Function<IColumn, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.selectWithFun(columnName, f);
    }
}
