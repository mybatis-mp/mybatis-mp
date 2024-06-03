package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;
import db.sql.api.cmd.executor.IQuery;

import java.io.Serializable;
import java.util.Collection;

public interface IInGetterCompare<RV> {

    default <T> RV in(Getter<T> column, IQuery query) {
        return this.in(true, column, 1, query);
    }

    default <T> RV in(boolean when, Getter<T> column, IQuery query) {
        return this.in(when, column, 1, query);
    }

    default <T> RV in(Getter<T> column, int storey, IQuery query) {
        return this.in(true, column, storey, query);
    }

    <T> RV in(boolean when, Getter<T> column, int storey, IQuery query);

    default <T> RV in(Getter<T> column, Serializable... values) {
        return this.in(true, column, 1, values);
    }

    default <T> RV in(boolean when, Getter<T> column, Serializable... values) {
        return this.in(when, column, 1, values);
    }

    default <T> RV in(Getter<T> column, int storey, Serializable[] values) {
        return this.in(true, column, storey, values);
    }

    <T> RV in(boolean when, Getter<T> column, int storey, Serializable[] values);

    default <T> RV in(Getter<T> column, Collection<? extends Serializable> values) {
        return this.in(true, column, 1, values);
    }

    default <T> RV in(boolean when, Getter<T> column, Collection<? extends Serializable> values) {
        return this.in(when, column, 1, values);
    }

    default <T> RV in(Getter<T> column, int storey, Collection<? extends Serializable> values) {
        return this.in(true, column, storey, values);
    }

    <T> RV in(boolean when, Getter<T> column, int storey, Collection<? extends Serializable> values);
}
