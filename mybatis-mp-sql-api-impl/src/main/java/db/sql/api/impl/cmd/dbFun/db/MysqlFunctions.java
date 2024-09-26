package db.sql.api.impl.cmd.dbFun.db;

import db.sql.api.Cmd;

import java.io.Serializable;

public class MysqlFunctions {

    private final Cmd key;

    public MysqlFunctions(Cmd key) {
        this.key = key;
    }

    @SafeVarargs
    public final JsonExtract jsonExtract(String... paths) {
        return new JsonExtract(this.key, paths);
    }

    @SafeVarargs
    public final JsonContainsPath jsonContainsPath(String... paths) {
        return new JsonContainsPath(this.key, paths);
    }

    @SafeVarargs
    public final JsonContainsPath jsonContainsPath(boolean allMatch, String... paths) {
        return new JsonContainsPath(this.key, allMatch, paths);
    }

    public final JsonContains jsonContains(Serializable containValue) {
        return new JsonContains(this.key, containValue);
    }

    public final JsonContains jsonContains(Serializable containValue, String path) {
        return new JsonContains(this.key, containValue, path);
    }
}
