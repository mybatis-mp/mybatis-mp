package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.IOrderByDirection;

public interface IOrderByDatasetGetterMethod<SELF extends IOrderByDatasetGetterMethod> extends IBaseOrderByMethods {

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        return this.orderBy(dataset, ascOrderByDirection(), column);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDesc(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        return this.orderBy(dataset, descOrderByDirection(), column);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, ascOrderByDirection(), column);
    }

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDesc(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, descOrderByDirection(), column);
    }

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T> column);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, orderByDirection, column);
    }
}
