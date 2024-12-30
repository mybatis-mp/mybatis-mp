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

package db.sql.api.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.tookit.CmdUtils;

public class ConflictKeys<COLUMN extends Cmd> implements Cmd {

    private final COLUMN[] conflictKeys;

    public ConflictKeys(COLUMN[] conflictKeys) {
        this.conflictKeys = conflictKeys;
    }

    public COLUMN[] getConflictKeys() {
        return conflictKeys;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        IConflictAction duplicateAction = (IConflictAction) parent;
        if (duplicateAction.getUpdateSets() == null) {
            if (context.getDbType() == DbType.MYSQL || context.getDbType() == DbType.MARIA_DB || context.getDbType() == DbType.H2) {
                return sqlBuilder;
            }
        }
        for (COLUMN conflictKey : conflictKeys) {
            conflictKey.sql(module, this, context, sqlBuilder);
            sqlBuilder.append(",");
        }
        sqlBuilder.deleteCharAt(sqlBuilder.length() - 1);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.conflictKeys);
    }
}
