package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.io.Serializable;
import java.util.function.Predicate;

public interface INotBetweenGetterPredicateCompare<RV> {

    default <T, V2> RV notBetween(Getter<T> column, V2 value, V2 value2, Predicate<V2> predicate) {
        return notBetween(column, 1, value, value2, predicate);
    }

    default <T, V2> RV notBetween(Getter<T> column, int storey, V2 value, V2 value2, Predicate<V2> predicate) {
        return this.notBetween(predicate.test(value) && predicate.test(value2), column, storey, (Serializable) value, (Serializable) value2);
    }

    <T> RV notBetween(boolean when, Getter<T> column, int storey, Serializable value, Serializable value2);
}
