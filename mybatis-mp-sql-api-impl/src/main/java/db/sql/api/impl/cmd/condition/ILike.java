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

package db.sql.api.impl.cmd.condition;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.LikeMode;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.tookit.SqlConst;

public class ILike extends Like {

    public ILike(char[] operator, LikeMode mode, Cmd key, Cmd value) {
        super(operator, mode, key, value);
    }

    public ILike(LikeMode mode, Cmd key, Cmd value) {
        super(SqlConst.I_LIKE, mode, key, value);
    }

    public ILike(LikeMode mode, Cmd key, Object value) {
        this(mode, key, Methods.cmd(value));
    }

    boolean support(DbType dbType) {
        switch (dbType) {
            case PGSQL:
            case OPEN_GAUSS:
            case SQLITE:
            case KING_BASE:
                return true;
            default:
                return false;
        }
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (!this.support(context.getDbType())) {
            this.operator = this instanceof NotILike ? SqlConst.LIKE : SqlConst.NOT_LIKE;
        }
        if (context.getDbType() == DbType.ORACLE) {
            this.field = Methods.upper(this.field);
            this.value = Methods.upper(this.value);
        }
        return super.sql(module, parent, context, sqlBuilder);
    }
}
