package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IGroupByMethods<SELF extends IGroupByMethods,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd>
        extends
        IGroupByCmdMethod<SELF, COLUMN>,
        IGroupByGetterMethod<SELF, TABLE, TABLE_FIELD>,
        IGroupByMultiGetterMethod<SELF, TABLE, TABLE_FIELD>,

        IGroupByDatasetMethod<SELF>,
        IGroupByDatasetGetterMethod<SELF>,
        IGroupByDatasetMultiGetterMethod<SELF> {

    @Override
    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        for (Getter<T> column : columns) {
            this.groupBy(dataset, column);
        }
        return (SELF) this;
    }

    SELF groupBy(String columnName);

    SELF groupBy(String columnName, Function<IDatasetField, Cmd> f);

    default SELF groupBy(boolean when, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(columnName);
    }

    default SELF groupBy(boolean when, String columnName, Function<IDatasetField, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(columnName, f);
    }
}
