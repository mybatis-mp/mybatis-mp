package db.sql.api.impl.cmd.executor;

import db.sql.api.impl.cmd.CmdFactory;

/**
 * 子查询
 */
public class SubQuery extends AbstractSubQuery<SubQuery, CmdFactory> {

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
