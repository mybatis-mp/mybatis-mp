package db.sql.api.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.Getter;

public interface IDataset<T extends IDataset, FIELD> extends Cmd, Alias<T> {

    FIELD $(String name);

    <E> FIELD $(Getter<E> getter);
}
