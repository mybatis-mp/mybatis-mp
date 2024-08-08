package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Function;

public interface IGroupByDatasetMultiGetterMethod<SELF extends IGroupByDatasetMultiGetterMethod> {

    <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns);

    default <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, columns);
    }

    //---

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], Cmd> f);

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF groupBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(dataset, getterFields, f);
    }
}
