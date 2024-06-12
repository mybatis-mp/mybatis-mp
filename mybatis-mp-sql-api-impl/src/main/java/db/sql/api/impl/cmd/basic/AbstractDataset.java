package db.sql.api.impl.cmd.basic;

import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

public abstract class AbstractDataset<T extends AbstractDataset<T, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> implements IDataset<T, DATASET_FIELD> {

    @Override
    public DATASET_FIELD $(String name) {
        return (DATASET_FIELD) new DatasetField(this, name);
    }

}
