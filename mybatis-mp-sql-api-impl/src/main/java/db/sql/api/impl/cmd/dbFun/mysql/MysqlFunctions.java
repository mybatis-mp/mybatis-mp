package db.sql.api.impl.cmd.dbFun.mysql;

import db.sql.api.Cmd;
import db.sql.api.impl.cmd.Methods;

import java.io.Serializable;

public class MysqlFunctions {

    private final Cmd key;

    public MysqlFunctions(Cmd key) {
        this.key = key;
    }

    @SafeVarargs
    public final JsonExtract jsonExtract(String... paths) {
        return Methods.mysqlJsonExtract(this.key, paths);
    }

    @SafeVarargs
    public final JsonContainsPath jsonContainsPath(String... paths) {
        return Methods.mysqlJsonContainsPath(this.key, paths);
    }

    @SafeVarargs
    public final JsonContainsPath jsonContainsPath(boolean allMatch, String... paths) {
        return Methods.mysqlJsonContainsPath(this.key, allMatch, paths);
    }

    public final JsonContains jsonContains(Serializable containValue) {
        return Methods.mysqlJsonContains(this.key, containValue);
    }

    public final JsonContains jsonContains(Serializable containValue, String path) {
        return Methods.mysqlJsonContains(this.key, containValue, path);
    }

    public final FindInSet findInSet(String str) {
        return Methods.mysqlFindInSet(this.key, str);
    }

    public final FindInSet findInSet(Number value) {
        return Methods.mysqlFindInSet(this.key, value);
    }

    @SafeVarargs
    public final Field filed(Object... values) {
        return Methods.mysqlFiled(key, values);
    }

    public final FromUnixTime fromUnixTime() {
        return Methods.mysqlFromUnixTime(this.key);
    }

    public final Md5 md5() {
        return Methods.mysqlMd5(this.key);
    }
}
