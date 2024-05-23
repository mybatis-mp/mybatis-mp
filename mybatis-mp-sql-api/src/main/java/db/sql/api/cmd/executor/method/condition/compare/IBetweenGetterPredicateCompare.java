package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.io.Serializable;
import java.util.function.Predicate;

public interface IBetweenGetterPredicateCompare<RV> {

    default <T, V2> RV between(Getter<T> column, V2 value, V2 value2, Predicate<V2> predicate) {
        return between(column, 1, value, value2, predicate);
    }

    default <T, V2> RV between(Getter<T> column, int storey, V2 value, V2 value2, Predicate<V2> predicate) {
        return this.between(predicate.test(value) && predicate.test(value2), column, storey, (Serializable) value, (Serializable) value2);
    }

    <T> RV between(boolean when, Getter<T> column, int storey, Serializable value, Serializable value2);
}
