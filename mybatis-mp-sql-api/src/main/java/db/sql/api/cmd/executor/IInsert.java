package db.sql.api.cmd.executor;

import db.sql.api.Getter;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.struct.insert.IInsertFields;
import db.sql.api.cmd.struct.insert.IInsertSelect;
import db.sql.api.cmd.struct.insert.IInsertTable;
import db.sql.api.cmd.struct.insert.IInsertValues;

import java.util.List;
import java.util.function.Consumer;

public interface IInsert<SELF extends IInsert,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        V,
        INSERT_TABLE extends IInsertTable<TABLE>,
        INSERT_FIELD extends IInsertFields<TABLE_FIELD>,
        INSERT_VALUE extends IInsertValues<V>,
        INSERT_SELECT extends IInsertSelect<IQuery>
        >
        extends IExecutor<SELF, TABLE, TABLE_FIELD> {


    INSERT_TABLE $insert(TABLE table);

    INSERT_FIELD $field(TABLE_FIELD... fields);

    INSERT_FIELD $field(List<TABLE_FIELD> fields);

    INSERT_VALUE $values(List<V> values);

    INSERT_SELECT $fromSelect(IQuery query);


    default SELF insert(TABLE table) {
        $insert(table);
        return (SELF) this;
    }

    SELF insert(Class entity);

    SELF insert(Class entity, Consumer<TABLE> consumer);

    SELF insertIgnore();

    default SELF field(TABLE_FIELD... fields) {
        $field(fields);
        return (SELF) this;
    }

    default SELF field(List<TABLE_FIELD> fields) {
        $field(fields);
        return (SELF) this;
    }

    <T> SELF field(Getter<T>... fields);

    default SELF values(List<Object> values) {
        return this.values(values, false);
    }

    SELF values(List<Object> values, boolean enableNull);

    SELF fromSelect(IQuery query);

    INSERT_TABLE getInsertTable();

    INSERT_FIELD getInsertFields();

    INSERT_VALUE getInsertValues();
}
