package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.util.function.Predicate;

public interface IGteGetterPredicateCompare<RV, V> {

    default <T, V2> RV gte(Getter<T> column, V2 value, Predicate<V2> predicate) {
        return this.gte(column, 1, value, predicate);
    }

    default <T, V2> RV gte(Getter<T> column, int storey, V2 value, Predicate<V2> predicate) {
        return gte(predicate.test(value), column, storey, (V) value);
    }

    <T> RV gte(boolean when, Getter<T> column, int storey, V value);
}
