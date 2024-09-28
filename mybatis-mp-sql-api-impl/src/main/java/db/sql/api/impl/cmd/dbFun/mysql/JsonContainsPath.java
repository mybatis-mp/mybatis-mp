package db.sql.api.impl.cmd.dbFun.mysql;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.dbFun.BasicFunction;
import db.sql.api.impl.tookit.SqlConst;

public class JsonContainsPath extends BasicFunction<JsonContainsPath> {

    private final String[] paths;

    private final boolean allMatch;

    public JsonContainsPath(Cmd key, String[] paths) {
        this(key, true, paths);
    }

    public JsonContainsPath(Cmd key,boolean allMatch, String[] paths) {
        super(SqlConst.JSON_CONTAINS_PATH, key);
        this.allMatch=allMatch;
        this.paths = paths;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(operator).append(SqlConst.BRACKET_LEFT);;
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER);
        sqlBuilder.append(this.allMatch?"'ONE'":"'ALL'");
        for (String path : paths) {
            sqlBuilder.append(SqlConst.DELIMITER);
            sqlBuilder.append(SqlConst.SINGLE_QUOT);
            sqlBuilder.append(path);
            sqlBuilder.append(SqlConst.SINGLE_QUOT);
        }
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }
}
