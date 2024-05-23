package db.sql.api.cmd.struct.query;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.tookit.CmdUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Withs implements Cmd {

    private List<IWith> withs;

    public void add(IWith with) {
        if (withs == null) {
            withs = new LinkedList<>();
        }
        withs.add(with);
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (withs == null || withs.isEmpty()) {
            return sqlBuilder;
        }
        CmdUtils.join(module, this, context, sqlBuilder, this.withs, ",".toCharArray());
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.withs);
    }

    public List<IWith> getUnions() {
        return Collections.unmodifiableList(this.withs);
    }
}
