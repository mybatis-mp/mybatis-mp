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

package db.sql.api.impl.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IConflict;
import db.sql.api.cmd.basic.IConflictAction;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.executor.AbstractInsert;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.function.Consumer;

public class Conflict<T> implements IConflict<T>, Cmd {

    private final CmdFactory cmdFactory;

    private String[] conflictKeys;

    private ConflictAction<T> conflictAction;

    private boolean hasChecked = false;

    public Conflict(CmdFactory cmdFactory) {
        this.cmdFactory = cmdFactory;
        conflictAction = new ConflictAction<>(cmdFactory);
    }

    @Override
    public IConflict<T> conflictKeys(String[] conflictKeys) {
        this.conflictKeys = conflictKeys;
        return this;
    }

    @Override
    public IConflict<T> conflictKeys(Getter<T>[] conflictKeys) {
        if (conflictKeys != null && conflictKeys.length > 0) {
            String[] array = new String[conflictKeys.length];
            for (int i = 0; i < conflictKeys.length; i++) {
                array[i] = this.cmdFactory.columnName(conflictKeys[i]);
            }
            this.conflictKeys = array;
        } else {
            this.conflictKeys = null;
        }
        return this;
    }

    @Override
    public void onConflict(Consumer<IConflictAction> action) {
        action.accept(this.conflictAction);
    }

    //增加默认 争议key
    public final void addDefaultConflictKeys(AbstractInsert insert, DbType dbType) {
        if (this.conflictKeys != null && this.conflictKeys.length > 0) {
            return;
        }

        if (dbType == DbType.ORACLE
                || ((dbType == DbType.PGSQL || dbType == DbType.KING_BASE) && !conflictAction.isDoNothing())
                || ((dbType == DbType.OPEN_GAUSS || dbType == DbType.SQLITE) && conflictAction.getConflictUpdate() == null)) {
            String[] conflictKeys = insert.getInsertTable().getTable().getIds();
            if (conflictKeys != null && conflictKeys.length > 0) {
                this.conflictKeys = conflictKeys;
            }
        }
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if (this.conflictAction == null) {
            throw new IllegalStateException("conflict action not set");
        }
        //增加默认的争议key
        this.addDefaultConflictKeys((AbstractInsert) module, context.getDbType());

        if (context.getDbType() == DbType.MYSQL || context.getDbType() == DbType.MARIA_DB || context.getDbType() == DbType.H2) {
            if (!conflictAction.isDoNothing()) {
                sqlBuilder.append(" ON DUPLICATE KEY");
            }
        } else if (context.getDbType() == DbType.OPEN_GAUSS) {
            sqlBuilder.append(" ON DUPLICATE KEY");
        } else if (context.getDbType() == DbType.PGSQL || context.getDbType() == DbType.KING_BASE || context.getDbType() == DbType.SQLITE) {
            sqlBuilder.append(" ON CONFLICT");
            if (this.conflictKeys != null) {
                sqlBuilder.append(SqlConst.BRACKET_LEFT);
                for (int i = 0; i < this.conflictKeys.length; i++) {
                    if (i != 0) {
                        sqlBuilder.append(SqlConst.DELIMITER);
                    }
                    sqlBuilder.append(context.getDbType().wrap(this.conflictKeys[i]));
                }
                sqlBuilder.append(SqlConst.BRACKET_RIGHT);
            }
        }

        this.conflictAction.sql(module, this, context, sqlBuilder);
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.conflictAction);
    }

    public String[] getConflictKeys() {
        return conflictKeys;
    }

    public ConflictAction<T> getConflictAction() {
        return conflictAction;
    }
}
