package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;
import db.sql.api.cmd.LikeMode;

import java.util.function.Predicate;

public interface ILikeGetterPredicateCompare<RV> {

    default <T> RV like(Getter<T> column, String value, Predicate<String> predicate) {
        return this.like(column, 1, value, predicate);
    }

    default <T> RV like(Getter<T> column, int storey, String value, Predicate<String> predicate) {
        return like(LikeMode.DEFAULT, column, storey, value, predicate);
    }

    default <T> RV like(LikeMode mode, Getter<T> column, String value, Predicate<String> predicate) {
        return like(mode, column, 1, value, predicate);
    }

    default <T> RV like(LikeMode mode, Getter<T> column, int storey, String value, Predicate<String> predicate) {
        return like(predicate.test(value), mode, column, storey, value);
    }

    <T> RV like(boolean when, LikeMode mode, Getter<T> column, int storey, String value);
}
