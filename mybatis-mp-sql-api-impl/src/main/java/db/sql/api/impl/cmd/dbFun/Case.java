package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.BasicValue;
import db.sql.api.impl.cmd.basic.Condition;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static db.sql.api.impl.tookit.SqlConst.CASE;

public class Case extends BasicFunction<Case> {

    private final List<Cmd> values = new LinkedList<>();

    public Case() {
        super(CASE, null);
    }

    public Case when(Condition condition, Cmd then) {
        values.add(new CaseWhen(condition, then));
        return this;
    }

    public Case when(Condition condition, Serializable then) {
        return this.when(condition, Methods.convert(then));
    }

    public Case when(boolean when, Condition condition, Serializable then) {
        if (!when) {
            return this;
        }
        return this.when(condition, then);
    }

    public <V extends Serializable> Case when(Condition condition, V then, Predicate<V> predicate) {
        return this.when(predicate.test(then), condition, then);
    }

    public Case else_(Cmd then) {
        values.add(then);
        return this;
    }

    public Case else_(Serializable then) {
        return this.else_(Methods.convert(then));
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.BRACKET_LEFT).append(operator);
        for (Cmd item : values) {
            if (!(item instanceof CaseWhen)) {
                sqlBuilder.append(SqlConst.ELSE);
                if (context.getDbType() == DbType.DB2 && item instanceof BasicValue) {
                    //没办法 DB2 数据库 ELSE 部分不支持 ?
                    BasicValue basicValue = (BasicValue) item;
                    if (basicValue.getValue() instanceof Number) {
                        sqlBuilder.append(basicValue.getValue());
                    } else {
                        sqlBuilder.append("'").append(basicValue.getValue()).append("'");
                    }
                    continue;
                }
            }
            item.sql(module, this, context, sqlBuilder);
        }
        sqlBuilder.append(SqlConst.END);
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.values);
    }
}

