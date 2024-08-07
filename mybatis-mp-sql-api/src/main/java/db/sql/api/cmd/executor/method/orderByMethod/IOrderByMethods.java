package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.*;

import java.util.function.Function;

public interface IOrderByMethods<SELF extends IOrderByMethods,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd>
        extends

        IOrderByCmdMethod<SELF, COLUMN>,
        IOrderByGetterMethod<SELF, TABLE, TABLE_FIELD>,
        IOrderByMultiGetterMethod<SELF, TABLE, TABLE_FIELD>,

        IOrderByDatasetMethod<SELF>,
        IOrderByDatasetGetterMethod<SELF>,
        IOrderByDatasetMultiGetterMethod<SELF> {


    default SELF orderBy(String columnName) {
        return this.orderBy(ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(String columnName) {
        return this.orderBy(descOrderByDirection(), columnName);
    }

    default SELF orderBy(boolean when, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), columnName);
    }

    default SELF orderByDesc(boolean when, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), columnName);
    }

    default SELF orderBy(String columnName, Function<IDatasetField, Cmd> f) {
        return this.orderBy(ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDesc(String columnName, Function<IDatasetField, Cmd> f) {
        return this.orderBy(descOrderByDirection(), columnName, f);
    }

    default SELF orderBy(boolean when, String columnName, Function<IDatasetField, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(ascOrderByDirection(), columnName, f);
    }

    default SELF orderByDesc(boolean when, String columnName, Function<IDatasetField, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(descOrderByDirection(), columnName, f);
    }

    SELF orderBy(IOrderByDirection orderByDirection, String columnName);

    SELF orderBy(IOrderByDirection orderByDirection, String columnName, Function<IDatasetField, Cmd> f);

    @Override
    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T>... columns) {
        for (Getter<T> column : columns) {
            this.orderBy(dataset, orderByDirection, column);
        }
        return (SELF) this;
    }

    default SELF orderBy(boolean when, IOrderByDirection orderByDirection, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, columnName);
    }

    default SELF orderBy(boolean when, IOrderByDirection orderByDirection, String columnName, Function<IDatasetField, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(orderByDirection, columnName, f);
    }
}
