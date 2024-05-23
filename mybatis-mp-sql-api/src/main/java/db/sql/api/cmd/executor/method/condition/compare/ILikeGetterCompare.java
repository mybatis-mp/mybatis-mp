package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;
import db.sql.api.cmd.LikeMode;

public interface ILikeGetterCompare<RV> {

    default <T> RV like(Getter<T> column, String value) {
        return like(true, LikeMode.DEFAULT, column, 1, value);
    }

    default <T> RV like(boolean when, Getter<T> column, String value) {
        return this.like(when, LikeMode.DEFAULT, column, 1, value);
    }

    default <T> RV like(Getter<T> column, String value, int storey) {
        return like(column, storey, value, true);
    }

    default <T> RV like(Getter<T> column, int storey, String value, boolean when) {
        return this.like(when, LikeMode.DEFAULT, column, storey, value);
    }

    default <T> RV like(LikeMode mode, Getter<T> column, String value) {
        return like(mode, column, 1, value);
    }

    default <T> RV like(boolean when, LikeMode mode, Getter<T> column, String value) {
        return this.like(when, mode, column, 1, value);
    }

    default <T> RV like(LikeMode mode, Getter<T> column, int storey, String value) {
        return this.like(true, mode, column, storey, value);
    }

    <T> RV like(boolean when, LikeMode mode, Getter<T> column, int storey, String value);
}
