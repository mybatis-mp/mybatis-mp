package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;
import db.sql.api.cmd.executor.IQuery;

import java.io.Serializable;
import java.util.List;

public interface INotInGetterCompare<RV> {

    default <T> RV notIn(Getter<T> column, IQuery query) {
        return this.notIn(true, column, 1, query);
    }

    default <T> RV notIn(Getter<T> column, boolean when, IQuery query) {
        return this.notIn(when, column, 1, query);
    }

    default <T> RV notIn(Getter<T> column, int storey, IQuery query) {
        return this.notIn(true, column, storey, query);
    }

    <T> RV notIn(boolean when, Getter<T> column, int storey, IQuery query);

    default <T> RV notIn(Getter<T> column, Serializable... values) {
        return this.notIn(true, column, 1, values);
    }

    default <T> RV notIn(boolean when, Getter<T> column, Serializable... values) {
        return this.notIn(when, column, 1, values);
    }

    default <T> RV notIn(Getter<T> column, int storey, Serializable[] values) {
        return this.notIn(true, column, storey, values);
    }

    <T> RV notIn(boolean when, Getter<T> column, int storey, Serializable[] values);

    default <T> RV notIn(Getter<T> column, List<? extends Serializable> values) {
        return this.notIn(true, column, 1, values);
    }

    default <T> RV notIn(boolean when, Getter<T> column, List<? extends Serializable> values) {
        return this.notIn(when, column, 1, values);
    }

    default <T> RV notIn(Getter<T> column, int storey, List<? extends Serializable> values) {
        return this.notIn(true, column, storey, values);
    }

    <T> RV notIn(boolean when, Getter<T> column, int storey, List<? extends Serializable> values);
}
