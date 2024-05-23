package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;
import db.sql.api.cmd.LikeMode;

import java.util.function.Predicate;

public interface INotLikeGetterPredicateCompare<RV> {
    default <T> RV notLike(Getter<T> column, String value, Predicate<String> predicate) {
        return this.notLike(column, 1, value, predicate);
    }

    default <T> RV notLike(Getter<T> column, int storey, String value, Predicate<String> predicate) {
        return notLike(LikeMode.DEFAULT, column, storey, value, predicate);
    }

    default <T> RV notLike(LikeMode mode, Getter<T> column, String value, Predicate<String> predicate) {
        return notLike(mode, column, 1, value, predicate);
    }

    default <T> RV notLike(LikeMode mode, Getter<T> column, int storey, String value, Predicate<String> predicate) {
        return notLike(predicate.test(value), mode, column, storey, value);
    }

    <T> RV notLike(boolean when, LikeMode mode, Getter<T> column, int storey, String value);
}
