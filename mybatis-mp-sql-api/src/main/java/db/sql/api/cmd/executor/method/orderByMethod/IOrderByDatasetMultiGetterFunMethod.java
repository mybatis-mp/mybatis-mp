package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.IOrderByDirection;

import java.util.function.Function;

public interface IOrderByDatasetMultiGetterFunMethod<SELF extends IOrderByDatasetMultiGetterFunMethod> extends IBaseOrderByMethods {

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, GetterField[] getterFields, Function<IDatasetField[], Cmd> f);

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], Cmd> f) {
        return this.orderBy(dataset, ascOrderByDirection(), getterFields, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, ascOrderByDirection(), getterFields, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDesc(IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], Cmd> f) {
        return this.orderBy(dataset, descOrderByDirection(), getterFields, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderByDesc(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, GetterField[] getterFields, Function<IDatasetField[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, descOrderByDirection(), getterFields, f);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF orderBy(boolean when, IDataset<DATASET, DATASET_FIELD> dataset, IOrderByDirection orderByDirection, GetterField[] getterFields, Function<IDatasetField[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(dataset, orderByDirection, getterFields, f);
    }


}
