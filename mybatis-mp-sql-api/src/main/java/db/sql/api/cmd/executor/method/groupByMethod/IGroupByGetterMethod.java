package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Getter;

public interface IGroupByGetterMethod<SELF extends IGroupByGetterMethod> {

    default <T> SELF groupBy(Getter<T> column) {
        return this.groupBy(column, 1);
    }

    <T> SELF groupBy(Getter<T> column, int storey);

    default <T> SELF groupBy(boolean when, Getter<T> column) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(column, 1);
    }

    default <T> SELF groupBy(boolean when, Getter<T> column, int storey) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(column, storey);
    }
}
