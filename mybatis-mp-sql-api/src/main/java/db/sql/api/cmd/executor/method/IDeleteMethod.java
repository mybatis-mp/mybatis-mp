package db.sql.api.cmd.executor.method;

import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

public interface IDeleteMethod<SELF extends IDeleteMethod> {


    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF delete(IDataset<DATASET, DATASET_FIELD>... tables);

    SELF delete(Class<?>... entities);
}
