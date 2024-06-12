package db.sql.api.cmd.basic;

public interface ITableField<T extends ITableField<T, TABLE>, TABLE extends ITable<TABLE, T>> extends IDatasetField<T> {

    @Override
    TABLE getTable();
}
