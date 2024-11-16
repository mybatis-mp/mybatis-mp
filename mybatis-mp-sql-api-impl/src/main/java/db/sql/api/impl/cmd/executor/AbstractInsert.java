package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.executor.IInsert;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.NULL;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.struct.insert.InsertFields;
import db.sql.api.impl.cmd.struct.insert.InsertSelect;
import db.sql.api.impl.cmd.struct.insert.InsertTable;
import db.sql.api.impl.cmd.struct.insert.InsertValues;
import db.sql.api.impl.tookit.Objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;


public abstract class AbstractInsert<SELF extends AbstractInsert<SELF, CMD_FACTORY>,
        CMD_FACTORY extends CmdFactory
        >
        extends BaseExecutor<SELF, CMD_FACTORY>
        implements IInsert<SELF,
        Table,
        TableField,
        Cmd,
        InsertTable,
        InsertFields,
        InsertValues,
        InsertSelect
        > {

    protected final CMD_FACTORY $;
    protected InsertTable insertTable;
    protected InsertFields insertFields;
    protected InsertValues insertValues;
    protected InsertSelect insertSelect;

    public AbstractInsert(CMD_FACTORY $) {
        this.$ = $;
    }

    public <T> TableField $(Getter<T> getter) {
        return this.$(getter, 1);
    }

    public <T> TableField $(Getter<T> getter, int storey) {
        return $().field(getter, storey);
    }

    public Table $(Class entityType) {
        return this.$(entityType, 1);
    }

    public Table $(Class entityType, int storey) {
        return $().table(entityType, storey);
    }

    public TableField $(Class entityType, String fieldName) {
        return this.$(entityType, fieldName, 1);
    }

    public TableField $(Class entityType, String fieldName, int storey) {
        return $().field(entityType, fieldName, storey);
    }

    @Override
    public CMD_FACTORY $() {
        return $;
    }

    @Override
    public InsertTable $insert(Table table) {
        if (this.insertTable == null) {
            this.insertTable = new InsertTable(table);
            this.append(this.insertTable);
        }
        return this.insertTable;
    }

    @Override
    public InsertFields $fields(TableField... fields) {
        if (this.insertFields == null) {
            this.insertFields = new InsertFields();
            this.append(this.insertFields);
        }
        this.insertFields.field(fields);
        return this.insertFields;
    }

    @Override
    public InsertFields $fields(List<TableField> fields) {
        if (this.insertFields == null) {
            this.insertFields = new InsertFields();
            this.append(this.insertFields);
        }
        this.insertFields.field(fields);
        return this.insertFields;
    }

    @Override
    public InsertValues $values(List<Cmd> values) {
        if (this.insertValues == null) {
            this.insertValues = new InsertValues();
            this.append(this.insertValues);
        }
        this.insertValues.add(values);
        return this.insertValues;
    }

    @Override
    public InsertSelect $fromSelect(IQuery query) {
        if (this.insertSelect != null) {
            this.cmds.remove(this.insertSelect);
        }
        this.insertSelect = new InsertSelect(query);
        this.cmds.add(this.insertSelect);
        return this.insertSelect;
    }

    @Override
    public SELF insert(Class entity) {
        return this.insert($.table(entity));
    }

    @Override
    public SELF insert(Class entity, Consumer<Table> consumer) {
        this.insert(entity);
        consumer.accept(this.getInsertTable().getTable());
        return (SELF) this;
    }

    @Override
    public SELF insertIgnore() {
        this.insertTable.setInsertIgnore(true);
        return (SELF) this;
    }

    @Override
    public <T> SELF fields(Getter<T>... fields) {
        TableField[] tableField = new TableField[fields.length];
        for (int i = 0; i < fields.length; i++) {
            tableField[i] = $.field(fields[i]);
        }
        return this.fields(tableField);
    }

    @Override
    public SELF values(List<Object> values, boolean enableNull) {
        List<Cmd> cmdValues = new ArrayList<>(values.size());
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if (enableNull && Objects.isNull(value)) {
                cmdValues.add(NULL.NULL);
                continue;
            }
            if (value instanceof Cmd) {
                cmdValues.add(Methods.cmd(value));
                continue;
            }
            TableField tableField = this.insertFields.getFields().get(i);
            cmdValues.add(Methods.cmd(tableField.paramWrap(value)));
        }
        this.$values(cmdValues);
        return (SELF) this;
    }

    @Override
    public SELF fromSelect(IQuery query) {
        this.$fromSelect(query);
        return (SELF) this;
    }

    @Override
    public InsertTable getInsertTable() {
        return this.insertTable;
    }

    @Override
    public InsertFields getInsertFields() {
        return this.insertFields;
    }

    @Override
    public InsertValues getInsertValues() {
        return this.insertValues;
    }

    public InsertSelect getInsertSelect() {
        return this.insertSelect;
    }

    @Override
    void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts) {
        int i = 0;
        cmdSorts.put(InsertTable.class, i += 10);
        cmdSorts.put(InsertFields.class, i += 10);
        cmdSorts.put(InsertValues.class, i += 10);
        cmdSorts.put(InsertSelect.class, i += 10);
    }


    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        return super.sql(module, parent, context, sqlBuilder);
    }
}
