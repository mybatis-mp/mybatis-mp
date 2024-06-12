package db.sql.api.impl.cmd.executor;

import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.DatasetField;

/**
 * 子查询
 */
public class WithQuery extends AbstractWithQuery<WithQuery, CmdFactory> implements db.sql.api.cmd.basic.IDataset<WithQuery, DatasetField> {

    private final String name;

    public WithQuery(String name) {
        super(new CmdFactory("wt"));
        this.name = name;
    }

    @Override
    public WithQuery as(String alias) {
        throw new RuntimeException("not support");
    }
}
