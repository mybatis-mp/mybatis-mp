package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.basic.AbstractField;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.condition.NotIn;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.io.Serializable;

public abstract class BasicFunction<T extends BasicFunction<T>> extends AbstractField<T> implements Function<T>, FunctionInterface {

    protected final char[] operator;

    protected final Cmd key;

    public BasicFunction(char[] operator, Cmd key) {
        this.operator = operator;
        this.key = key;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.key);
    }

    /*****************以下为去除广告*******************/

    @Override
    @SafeVarargs
    public final ConcatAs concatAs(String split, Serializable... values) {
        return super.concatAs(split, values);
    }

    @Override
    @SafeVarargs
    public final ConcatAs concatAs(String split, Cmd... values) {
        return super.concatAs(split, values);
    }

    @Override
    @SafeVarargs
    public final Concat concat(Serializable... values) {
        return super.concat(values);
    }

    @Override
    @SafeVarargs
    public final Concat concat(Cmd... values) {
        return super.concat(values);
    }

    @Override
    @SafeVarargs
    public final In in(Serializable... values) {
        return super.in(values);
    }

    @Override
    @SafeVarargs
    public final NotIn notIn(Serializable... values) {
        return super.notIn(values);
    }
}
