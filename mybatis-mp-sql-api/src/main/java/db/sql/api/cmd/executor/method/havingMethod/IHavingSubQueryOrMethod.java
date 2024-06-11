package db.sql.api.cmd.executor.method.havingMethod;

import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.basic.IDataset;

import java.util.function.Function;

public interface IHavingSubQueryOrMethod<SELF extends IHavingSubQueryOrMethod, DATASET_FILED> {

    default SELF havingOr(IDataset dataset, String columnName, Function<DATASET_FILED, ICondition> f) {
        return this.havingOr(dataset, true, columnName, f);
    }

    SELF havingOr(IDataset dataset, boolean when, String columnName, Function<DATASET_FILED, ICondition> f);

    default <T> SELF havingOr(IDataset dataset, Getter<T> column, Function<DATASET_FILED, ICondition> f) {
        return havingOr(dataset, true, column, f);
    }

    <T> SELF havingOr(IDataset dataset, boolean when, Getter<T> column, Function<DATASET_FILED, ICondition> f);


    default <T> SELF havingOr(IDataset dataset, Function<DATASET_FILED[], ICondition> f, Getter<T>... columns) {
        return this.havingOr(dataset, true, f, columns);
    }

    <T> SELF havingOr(IDataset dataset, boolean when, Function<DATASET_FILED[], ICondition> f, Getter<T>... columns);


    default SELF havingOr(IDataset dataset, Function<DATASET_FILED[], ICondition> f, IColumnField... columnFields) {
        return this.havingOr(dataset, true, f, columnFields);
    }

    SELF havingOr(IDataset dataset, boolean when, Function<DATASET_FILED[], ICondition> f, IColumnField... columnFields);
}
