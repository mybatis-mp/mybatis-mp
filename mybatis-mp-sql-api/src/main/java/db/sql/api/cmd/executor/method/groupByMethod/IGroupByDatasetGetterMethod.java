package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Function;

public interface IGroupByDatasetGetterMethod<SELF extends IGroupByDatasetGetterMethod> {

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, column);
    }

    //---

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f);


    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column, Function<DATASET_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, column, f);
    }


}
