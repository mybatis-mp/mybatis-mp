package db.sql.api.cmd.basic;

public interface ITable<T extends ITable<T, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, T>> extends IDataset<T, TABLE_FIELD> {

}
