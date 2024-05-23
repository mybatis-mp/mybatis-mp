package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.util.function.Predicate;

public interface ILteGetterPredicateCompare<RV, V> {

    default <T, V2> RV lte(Getter<T> column, V2 value, Predicate<V2> predicate) {
        return this.lte(column, 1, value, predicate);
    }

    default <T, V2> RV lte(Getter<T> column, int storey, V2 value, Predicate<V2> predicate) {
        return lte(predicate.test(value), column, storey, (V) value);
    }

    <T> RV lte(boolean when, Getter<T> column, int storey, V value);
}
