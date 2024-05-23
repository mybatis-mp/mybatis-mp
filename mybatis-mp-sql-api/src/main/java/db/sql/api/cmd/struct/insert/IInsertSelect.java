package db.sql.api.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.cmd.executor.IQuery;

public interface IInsertSelect<Q extends IQuery> extends Cmd {

    Q getSelectQuery();

}
