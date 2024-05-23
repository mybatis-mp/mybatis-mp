package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.cmd.executor.ISubQuery;

public interface IOrderBySubQueryMultiGetterMethod<SELF extends IOrderBySubQueryMultiGetterMethod> extends IBaseOrderByMethods {

    default <T> SELF orderBy(ISubQuery subQuery, Getter<T>... columns) {
        return this.orderBy(subQuery, defaultOrderByDirection(), columns);
    }

    <T> SELF orderBy(ISubQuery subQuery, IOrderByDirection orderByDirection, Getter<T>... columns);

    default <T> SELF orderBy(boolean when, ISubQuery subQuery, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(subQuery, defaultOrderByDirection(), columns);
    }

    default <T> SELF orderBy(boolean when, ISubQuery subQuery, IOrderByDirection orderByDirection, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.orderBy(subQuery, orderByDirection, columns);
    }
}
