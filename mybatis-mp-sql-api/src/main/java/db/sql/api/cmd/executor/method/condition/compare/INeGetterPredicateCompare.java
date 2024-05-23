package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.util.function.Predicate;

public interface INeGetterPredicateCompare<RV, V> {

    default <T, V2> RV ne(Getter<T> column, V2 value, Predicate<V2> predicate) {
        return this.ne(column, 1, value, predicate);
    }

    default <T, V2> RV ne(Getter<T> column, int storey, V2 value, Predicate<V2> predicate) {
        return ne(predicate.test(value), column, storey, (V) value);
    }

    <T> RV ne(boolean when, Getter<T> column, int storey, V value);
}