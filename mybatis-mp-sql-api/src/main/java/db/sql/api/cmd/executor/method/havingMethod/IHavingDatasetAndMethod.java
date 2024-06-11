package db.sql.api.cmd.executor.method.havingMethod;

import db.sql.api.Getter;
import db.sql.api.cmd.IColumnField;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.executor.ISubQuery;

import java.util.function.Function;

public interface IHavingDatasetAndMethod<SELF extends IHavingDatasetAndMethod, DATASET_FILED> {

    default SELF havingAnd(IDataset dataset, String columnName, Function<DATASET_FILED, ICondition> f) {
        return this.havingAnd(dataset, true, columnName, f);
    }

    SELF havingAnd(IDataset dataset, boolean when, String columnName, Function<DATASET_FILED, ICondition> f);

    default <T> SELF havingAnd(IDataset dataset, Getter<T> column, Function<DATASET_FILED, ICondition> f) {
        return havingAnd(dataset, true, column, f);
    }

    <T> SELF havingAnd(IDataset dataset, boolean when, Getter<T> column, Function<DATASET_FILED, ICondition> f);

    default <T> SELF havingAnd(IDataset dataset, Function<DATASET_FILED[], ICondition> f, Getter<T>... columns) {
        return this.havingAnd(dataset, true, f, columns);
    }

    <T> SELF havingAnd(IDataset dataset, boolean when, Function<DATASET_FILED[], ICondition> f, Getter<T>... columns);

    default SELF havingAnd(IDataset dataset, Function<DATASET_FILED[], ICondition> f, IColumnField... columnFields) {
        return this.havingAnd(dataset, true, f, columnFields);
    }

    SELF havingAnd(IDataset dataset, boolean when, Function<DATASET_FILED[], ICondition> f, IColumnField... columnFields);

}
