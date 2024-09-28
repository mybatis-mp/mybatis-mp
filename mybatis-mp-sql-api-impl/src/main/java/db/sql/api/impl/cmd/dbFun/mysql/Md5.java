package db.sql.api.impl.cmd.dbFun.mysql;

import db.sql.api.Cmd;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.dbFun.BasicFunction;
import db.sql.api.impl.tookit.SqlConst;

public class Md5 extends BasicFunction<Md5> {

    public Md5(String str) {
        this(Methods.cmd(str));
    }

    public Md5(Cmd key) {
        super(SqlConst.MD5, key);
    }
}
