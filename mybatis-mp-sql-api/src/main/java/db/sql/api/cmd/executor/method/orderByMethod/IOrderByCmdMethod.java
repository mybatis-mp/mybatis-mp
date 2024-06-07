package db.sql.api.cmd.executor.method.orderByMethod;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IOrderByDirection;

import java.util.List;

public interface IOrderByCmdMethod<SELF extends IOrderByCmdMethod, COLUMN extends Cmd> extends IBaseOrderByMethods {

    default SELF orderBy(COLUMN column) {
        return this.orderBy(ascOrderByDirection(), column);
    }

    default SELF orderBy(COLUMN... columns) {
        return this.orderBy(ascOrderByDirection(), columns);
    }

    default SELF orderBy(List<COLUMN> columns) {
        return this.orderBy(ascOrderByDirection(), columns);
    }

    default SELF orderByDesc(COLUMN column) {
        return this.orderBy(descOrderByDirection(), column);
    }

    default SELF orderByDesc(COLUMN... columns) {
        return this.orderBy(descOrderByDirection(), columns);
    }

    default SELF orderByDesc(List<COLUMN> columns) {
        return this.orderBy(descOrderByDirection(), columns);
    }

    SELF orderBy(IOrderByDirection orderByDirection, COLUMN column);

    default SELF orderBy(IOrderByDirection orderByDirection, COLUMN... columns) {
        for (COLUMN column : columns) {
            this.orderBy(orderByDirection, column);
        }
        return (SELF) this;
    }

    default SELF orderBy(IOrderByDirection orderByDirection, List<COLUMN> columns) {
        for (COLUMN column : columns) {
            this.orderBy(orderByDirection, column);
        }
        return (SELF) this;
    }
}
