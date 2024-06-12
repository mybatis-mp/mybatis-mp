package db.sql.api.cmd.basic;

import db.sql.api.Cmd;

public interface IDataset<T extends IDataset<T, FIELD>, FIELD extends IDatasetField<FIELD>> extends Cmd, Alias<T> {

    FIELD $(String name);
}
