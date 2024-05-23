package db.sql.api.impl.cmd.struct;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.struct.IForUpdate;
import db.sql.api.impl.tookit.SqlConst;

public class ForUpdate implements IForUpdate<ForUpdate> {

    private boolean wait = true;

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(wait ? SqlConst.FOR_UPDATE : SqlConst.FOR_UPDATE_NO_WAIT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return false;
    }

    @Override
    public void setWait(boolean wait) {
        this.wait = wait;
    }
}
