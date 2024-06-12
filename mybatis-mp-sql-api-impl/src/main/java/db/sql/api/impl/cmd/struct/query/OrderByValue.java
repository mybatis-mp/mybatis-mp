package db.sql.api.impl.cmd.struct.query;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IOrderByDirection;
import db.sql.api.impl.cmd.basic.OrderByDirection;
import db.sql.api.impl.cmd.condition.IsNotNull;
import db.sql.api.impl.cmd.condition.IsNull;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.Objects;

public class OrderByValue implements Cmd {

    private final Cmd key;

    private final IOrderByDirection orderByDirection;

    public OrderByValue(IOrderByDirection orderByDirection, Cmd key) {
        if (key instanceof IOrderByDirection) {
            throw new RuntimeException();
        }
        this.key = key;
        this.orderByDirection = orderByDirection;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (!IOrderByDirection.isSupportNullsOrder(context.getDbType())) {
            Cmd nullDirectionCmd = null;
            if (orderByDirection == OrderByDirection.DESC_NULLS_FIRST || orderByDirection == OrderByDirection.ASC_NULLS_FIRST) {
                nullDirectionCmd = new IsNull(this.key);
            } else if (orderByDirection == OrderByDirection.DESC_NULLS_LAST || orderByDirection == OrderByDirection.ASC_NULLS_LAST) {
                nullDirectionCmd = new IsNotNull(this.key);
            }
            if (Objects.nonNull(nullDirectionCmd)) {
                nullDirectionCmd.sql(module, parent, context, sqlBuilder);
                sqlBuilder.append(SqlConst.DELIMITER);
            }
        }

        key.sql(module, this, context, sqlBuilder);
        orderByDirection.sql(module, this.key, context, sqlBuilder);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.key);
    }
}
