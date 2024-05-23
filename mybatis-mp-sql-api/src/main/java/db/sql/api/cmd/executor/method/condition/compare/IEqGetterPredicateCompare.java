package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.util.function.Predicate;

public interface IEqGetterPredicateCompare<RV, V> {

    default <T, V2> RV eq(Getter<T> column, V2 value, Predicate<V2> predicate) {
        return this.eq(column, 1, value, predicate);
    }

    default <T, V2> RV eq(Getter<T> column, int storey, V2 value, Predicate<V2> predicate) {
        return eq(predicate.test(value), column, storey, (V) value);
    }

    <T> RV eq(boolean when, Getter<T> column, int storey, V value);
}
