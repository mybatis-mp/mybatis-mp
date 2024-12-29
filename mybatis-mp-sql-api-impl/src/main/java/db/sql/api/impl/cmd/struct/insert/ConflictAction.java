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

package db.sql.api.impl.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.struct.insert.ConflictKeys;
import db.sql.api.cmd.struct.insert.IConflictAction;
import db.sql.api.cmd.struct.update.IUpdateSets;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.struct.update.UpdateSet;
import db.sql.api.impl.cmd.struct.update.UpdateSets;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public class ConflictAction implements IConflictAction<TableField, Cmd, UpdateSet>, Cmd {

    private ConflictKeys<TableField> conflictKeys;

    private UpdateSets updateSets;

    @Override
    public ConflictKeys getConflictKeys() {
        return conflictKeys;
    }

    public void setConflictKeys(ConflictKeys<TableField> conflictKeys) {
        this.conflictKeys = conflictKeys;
    }

    @Override
    public IUpdateSets<TableField, Cmd, UpdateSet> getUpdateSets() {
        return updateSets;
    }

    public void setUpdateSets(UpdateSets updateSets) {
        this.updateSets = updateSets;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {

        if (context.getDbType() == DbType.MYSQL || context.getDbType() == DbType.MARIA_DB || context.getDbType() == DbType.OPEN_GAUSS) {
            sqlBuilder.append(" ON DUPLICATE KEY");
        } else if (context.getDbType() == DbType.PGSQL || context.getDbType() == DbType.KING_BASE) {
            sqlBuilder.append(" ON CONFLICT");
            if (this.conflictKeys != null && this.conflictKeys.getConflictKeys().length > 0) {
                sqlBuilder.append(SqlConst.BRACKET_LEFT);
                this.conflictKeys.sql(module, this, context, sqlBuilder);
                sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            }
        }

        if (this.updateSets == null) {
            if (context.getDbType() == DbType.PGSQL || context.getDbType() == DbType.KING_BASE || context.getDbType() == DbType.OPEN_GAUSS) {
                sqlBuilder.append(" DO NOTHING");
            }
        } else {
            if (context.getDbType() == DbType.PGSQL || context.getDbType() == DbType.KING_BASE) {
                sqlBuilder.append(" DO UPDATE");
            } else if (context.getDbType() == DbType.OPEN_GAUSS) {
                sqlBuilder.append(" UPDATE");
            }
            this.updateSets.sql(module, this, context, sqlBuilder);
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.conflictKeys, this.updateSets);
    }
}
