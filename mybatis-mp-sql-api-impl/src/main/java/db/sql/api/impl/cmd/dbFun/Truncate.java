/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package db.sql.api.impl.cmd.dbFun;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.SqlConst;

public class Truncate extends BasicFunction<Truncate> {

    private final int precision;

    public Truncate(Cmd key, int precision) {
        super(null, key);
        this.precision = precision;
    }

    @Override
    public StringBuilder functionSql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(SqlConst.TRUNCATE(context.getDbType())).append(SqlConst.BRACKET_LEFT);
        this.key.sql(module, this, context, sqlBuilder);
        sqlBuilder.append(SqlConst.DELIMITER).append(this.precision);
        if (context.getDbType() == DbType.SQL_SERVER) {
            sqlBuilder.append(",1");
        }
        sqlBuilder.append(SqlConst.BRACKET_RIGHT);
        return sqlBuilder;
    }

}