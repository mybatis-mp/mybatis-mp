package db.sql.api.impl.cmd.basic;

public class TableField extends DatasetField<TableField, Table> {

    public TableField(Table table, String name) {
        super(table, name);
    }

    protected boolean canEqual(Object other) {
        return other instanceof TableField;
    }

    @Override
    public int hashCode() {
        int result = 1;
        Object $table = this.getTable().toString();
        result = result * 59 + ($table == null ? 43 : $table.hashCode());
        Object $name = this.getName();
        result = result * 59 + ($name == null ? 43 : $name.hashCode());
        Object $alias = this.getAlias();
        result = result * 59 + ($alias == null ? 43 : $alias.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof TableField)) {
            return false;
        } else {
            TableField other = (TableField) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$table = this.getTable();
                Object other$table = other.getTable();
                if (this$table != other$table) {
                    return false;
                }

                Object this$name = this.getName();
                Object other$name = other.getName();
                if (this$name == null) {
                    if (other$name != null) {
                        return false;
                    }
                } else if (!this$name.equals(other$name)) {
                    return false;
                }

                Object this$alias = this.getAlias();
                Object other$alias = other.getAlias();
                if (this$alias == null) {
                    return other$alias == null;
                }
                return this$alias.equals(other$alias);
            }
        }
    }
}
