package db.sql.api.cmd.basic;

import db.sql.api.Getter;

public interface ITable<T extends ITable<T, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, T>> extends IDataset<T, TABLE_FIELD> {

   <E> TABLE_FIELD $(Getter<E> column);

    String getName();
}
