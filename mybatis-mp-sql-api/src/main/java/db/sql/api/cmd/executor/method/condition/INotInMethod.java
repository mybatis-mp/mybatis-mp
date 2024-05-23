package db.sql.api.cmd.executor.method.condition;

import db.sql.api.cmd.executor.IQuery;
import db.sql.api.cmd.executor.method.condition.compare.INotInGetterCompare;

import java.io.Serializable;
import java.util.List;

public interface INotInMethod<RV, COLUMN> extends INotInGetterCompare<RV> {

    RV notIn(COLUMN column, IQuery query);

    RV notIn(COLUMN column, Serializable... values);

    RV notIn(COLUMN column, List<? extends Serializable> values);
}
