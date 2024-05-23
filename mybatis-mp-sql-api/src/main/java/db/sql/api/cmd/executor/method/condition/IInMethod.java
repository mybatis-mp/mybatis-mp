package db.sql.api.cmd.executor.method.condition;

import db.sql.api.cmd.executor.IQuery;
import db.sql.api.cmd.executor.method.condition.compare.IInGetterCompare;

import java.io.Serializable;
import java.util.List;

public interface IInMethod<RV, COLUMN> extends IInGetterCompare<RV> {

    RV in(COLUMN column, IQuery query);

    RV in(COLUMN column, Serializable... values);

    RV in(COLUMN column, List<? extends Serializable> values);
}
