package db.sql.api.cmd.executor.method;

import db.sql.api.cmd.executor.IWithQuery;

public interface IWithMethod<SELF> {
    SELF with(IWithQuery... withQuerys);
}
