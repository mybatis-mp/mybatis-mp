package db.sql.api.impl.cmd.executor;

import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Dataset;
import db.sql.api.impl.cmd.basic.DatasetField;

/**
 * 子查询
 */
public class SubQuery extends AbstractSubQuery<SubQuery, CmdFactory> implements Dataset<SubQuery, DatasetField> {

    private final String alias;

    public SubQuery(String alias) {
        super(new CmdFactory("st"));
        this.alias = alias;
    }

    @Override
    public String getAlias() {
        return alias;
    }

    @Override
    public SubQuery as(String alias) {
        throw new RuntimeException("not support");
    }


}
