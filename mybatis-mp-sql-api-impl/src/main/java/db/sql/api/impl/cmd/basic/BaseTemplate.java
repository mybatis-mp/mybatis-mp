package db.sql.api.impl.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.Alias;
import db.sql.api.impl.cmd.struct.query.Select;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.text.MessageFormat;
import java.util.Objects;

public abstract class BaseTemplate<T> implements Cmd, Alias<T> {

    protected final String template;
    protected final Cmd[] params;
    private String alias;

    public BaseTemplate(String template, Object... params) {
        this.template = template;
        if (Objects.nonNull(params)) {
            Cmd[] cmds = new Cmd[params.length];
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                cmds[i] = param instanceof Cmd ? (Cmd) param : new BasicValue(param);
            }
            this.params = cmds;
        } else {
            this.params = null;
        }
    }

    public BaseTemplate(String template, Cmd... params) {
        this.template = template;
        this.params = params;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public T as(String alias) {
        this.alias = alias;
        return (T) this;
    }

    /**
     * 拼接别名
     *
     * @param module
     * @param user
     * @param context
     * @param sqlBuilder
     */
    private void appendAlias(Cmd module, Cmd user, SqlBuilderContext context, StringBuilder sqlBuilder) {
        //拼接 select 的别名
        if (module instanceof Select && user instanceof Select) {
            if (this.getAlias() != null) {
                sqlBuilder.append(SqlConst.AS(context.getDbType()));
                sqlBuilder.append(this.getAlias());
            }
        }
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.BLANK).append(SqlConst.BRACKET_LEFT);
        String str = this.template;
        if (Objects.nonNull(params) && params.length > 0) {
            Object[] paramsStr = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                paramsStr[i] = params[i].sql(module, parent, context, new StringBuilder());
            }
            str = MessageFormat.format(this.template, paramsStr);
        }
        sqlBuilder.append(SqlConst.BLANK).append(str).append(SqlConst.BRACKET_RIGHT);
        this.appendAlias(module, parent, context, sqlBuilder);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        if (Objects.isNull(params)) {
            return false;
        }
        return CmdUtils.contain(cmd, params);
    }
}
