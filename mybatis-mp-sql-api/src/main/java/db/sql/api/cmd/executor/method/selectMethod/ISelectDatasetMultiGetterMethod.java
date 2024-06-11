package db.sql.api.cmd.executor.method.selectMethod;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;

public interface ISelectDatasetMultiGetterMethod<SELF extends ISelectDatasetMultiGetterMethod> {

    <T> SELF select(IDataset dataset, Getter<T>... columns);

    default <T> SELF select(boolean when, IDataset dataset, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.select(dataset, columns);
    }
}
