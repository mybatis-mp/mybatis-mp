package db.sql.api.impl.cmd.condition;

import db.sql.api.Cmd;
import db.sql.api.cmd.LikeMode;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.tookit.SqlConst;

public class NotLike extends Like {

    public NotLike(Cmd key, Cmd value) {
        this(LikeMode.DEFAULT, key, value);
    }

    public NotLike(Cmd key, Object value) {
        this(key, Methods.convert(value));
    }

    public NotLike(LikeMode mode, Cmd key, Cmd value) {
        super(SqlConst.NOT_LIKE, mode, key, value);
    }

    public NotLike(LikeMode mode, Cmd key, Object value) {
        super(SqlConst.NOT_LIKE, mode, key, Methods.convert(value));
    }
}
