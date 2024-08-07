package db.sql.api.impl.cmd.basic;


import db.sql.api.Cmd;
import db.sql.api.SQLMode;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.LikeMode;
import db.sql.api.impl.cmd.condition.Like;
import db.sql.api.impl.cmd.struct.query.OrderBy;
import db.sql.api.impl.cmd.struct.query.Select;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.Objects;

public class BasicValue extends AbstractField<BasicValue> {

    private final Object value;

    public BasicValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        Object originValue = this.value;

        if (value instanceof ValueWrap) {
            originValue = ((ValueWrap) value).getOriginValue();
        }

        if (Objects.nonNull(originValue) && originValue instanceof String && parent instanceof Like && ((Like) parent).getMode() != LikeMode.NONE) {
            originValue = originValue.toString().replaceAll("%", "\\\\%").replaceAll("_", "\\\\_%");
        }
        if (context.getSqlMode() == SQLMode.PRINT || module instanceof OrderBy) {
            if (originValue instanceof Number) {
                sqlBuilder.append(originValue);
            } else {
                sqlBuilder.append(SqlConst.SINGLE_QUOT(context.getDbType())).append(originValue).append(SqlConst.SINGLE_QUOT(context.getDbType()));
            }
        } else {
            sqlBuilder.append(context.addParam(value));
        }

        if (parent instanceof Select) {
            if (Objects.nonNull(this.getAlias())) {
                sqlBuilder.append(SqlConst.AS(context.getDbType())).append(this.getAlias());
            }
        }

        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.value);
    }
}
