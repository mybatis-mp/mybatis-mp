package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.util.function.Predicate;

public interface IGtGetterPredicateCompare<RV, V> {

    default <T, V2> RV gt(Getter<T> column, V2 value, Predicate<V2> predicate) {
        return this.gt(column, 1, value, predicate);
    }

    default <T, V2> RV gt(Getter<T> column, int storey, V2 value, Predicate<V2> predicate) {
        return gt(predicate.test(value), column, storey, (V) value);
    }

    <T> RV gt(boolean when, Getter<T> column, int storey, V value);
}
