package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.IOrderByDirection;

import java.util.function.Function;

public interface IOrderByDatasetMultiGetterFunMethod<SELF extends IOrderByDatasetMultiGetterFunMethod> extends IBaseOrderByMethods {

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columns);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDescWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columns);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columnFields);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDescWithFun(IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columnFields);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columns);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDescWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columns);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, ascOrderByDirection(), f, columnFields);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDescWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, descOrderByDirection(), f, columnFields);
    }

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Function<IDatasetField[], Cmd> f, Getter<T>... columns);

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Function<IDatasetField[], Cmd> f, IColumnField... columnFields);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Function<IDatasetField[], Cmd> f, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, orderByDirection, f, columns);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByWithFun(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Function<IDatasetField[], Cmd> f, IColumnField... columnFields) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderByWithFun(dataset, orderByDirection, f, columnFields);
    }
}
