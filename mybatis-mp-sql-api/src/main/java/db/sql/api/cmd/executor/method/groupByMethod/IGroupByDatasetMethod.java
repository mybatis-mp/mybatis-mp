package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Function;

public interface IGroupByDatasetMethod<SELF extends IGroupByDatasetMethod> {

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        return this.groupBy(dataset, columnName, null);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, columnName);
    }

    //---

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, Cmd> f);

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, String columnName, Function<DATASET_FIELD, Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, columnName, f);
    }
}
