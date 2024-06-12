package db.sql.api.impl.cmd;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterColumnField;
import db.sql.api.cmd.ICmdFactory;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.impl.cmd.basic.*;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.condition.NotIn;
import db.sql.api.impl.tookit.LambdaUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class CmdFactory extends Methods implements ICmdFactory<Table, TableField> {

    protected final Map<String, Table> tableCache = new HashMap<>(5);
    private final String tableAsPrefix;
    protected int tableNums = 0;

    public CmdFactory() {
        this("t");
    }

    public CmdFactory(String tableAsPrefix) {
        this.tableAsPrefix = tableAsPrefix;
    }

    public NULL NULL() {
        return NULL.NULL;
    }

    protected String tableAs(int storey, int tableNums) {
        String as = this.tableAsPrefix +
                (tableNums == 1 ? "" : tableNums);
        return as;
    }

    public Table cacheTable(Class entity, int storey) {
        return this.tableCache.get(String.format("%s.%s", entity.getName(), storey));
    }


    @Override
    public Table table(Class entity, int storey) {
        return tableCache.computeIfAbsent(entity.getName(), key -> {
            Table table = new Table(entity.getSimpleName());
            table.as(tableAs(storey, ++tableNums));
            return table;
        });
    }

    @Override
    public Table table(String tableName) {
        return new Table(tableName);
    }

    @Override
    public <T> String columnName(Getter<T> column) {
        return LambdaUtil.getName(column);
    }

    @Override
    public Column column(String columnName) {
        return new Column(columnName);
    }

    @Override
    public <T> TableField field(Getter<T> column, int storey) {
        LambdaUtil.LambdaFieldInfo fieldInfo = LambdaUtil.getFieldInfo(column);
        return this.field(fieldInfo.getType(), 1, fieldInfo.getName());
    }

    @Override
    public <T> TableField[] fields(int storey, Getter<T>... columns) {
        TableField[] tableFields = new TableField[columns.length];
        for (int i = 0; i < columns.length; i++) {
            tableFields[i] = field(columns[i], storey);
        }
        return tableFields;
    }

    @Override
    public TableField[] fields(GetterColumnField... getterColumnFields) {
        TableField[] tableFields = new TableField[getterColumnFields.length];
        for (int i = 0; i < getterColumnFields.length; i++) {
            GetterColumnField columnField = getterColumnFields[i];
            GetterColumnField getterColumnField = columnField;
            tableFields[i] = field(getterColumnField.getGetter(), getterColumnField.getStorey());
        }
        return tableFields;
    }

    public <T> TableField field(Table table, Getter<T> column) {
        return new TableField(table, columnName(column));
    }


    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD field(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        String filedName = LambdaUtil.getName(column);
        return (DATASET_FIELD) new DatasetField(dataset, filedName);
    }

    @Override
    public TableField field(Class entity, String filedName, int storey) {
        return this.field(entity, storey, filedName);
    }

    public TableField field(Table table, String columnName) {
        return new TableField(table, columnName);
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD field(IDataset<DATASET, DATASET_FIELD> dataset, String columnName) {
        return (DATASET_FIELD) new DatasetField(dataset, columnName);
    }

    @Override
    public <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD allField(IDataset<DATASET, DATASET_FIELD> dataset) {
        return (DATASET_FIELD) new AllField(dataset);
    }

    @Override
    public <T, R extends Cmd> R create(Getter<T> column, int storey, Function<TableField, R> RF) {
        return RF.apply(this.field(column, storey));
    }

    protected TableField field(Class clazz, int storey, String filedName) {
        Table table = table(clazz, storey);
        return new TableField(table, filedName);
    }

    public BasicValue value(Object value) {
        return new BasicValue(value);
    }

    public In in(Cmd main) {
        return new In(main);
    }

    public NotIn notIn(Cmd main) {
        return new NotIn(main);
    }
}
