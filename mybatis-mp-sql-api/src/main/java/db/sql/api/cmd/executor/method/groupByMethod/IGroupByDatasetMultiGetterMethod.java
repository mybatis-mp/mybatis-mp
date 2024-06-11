package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.executor.ISubQuery;

public interface IGroupByDatasetMultiGetterMethod<SELF extends IGroupByDatasetMultiGetterMethod> {

    <T> SELF groupBy(IDataset dataset, Getter<T>... columns);

    default <T> SELF groupBy(boolean when, IDataset dataset, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, columns);
    }
}
