package db.sql.api.impl.cmd.basic;

import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IParamWrap;


public class DatasetField extends AbstractDatasetField<DatasetField> implements IParamWrap {

    public DatasetField(IDataset dataset, String name) {
        super(dataset, name);
    }
}
