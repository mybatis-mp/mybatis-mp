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
import db.sql.api.cmd.struct.insert.IInsertTable;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.executor.AbstractInsert;
import db.sql.api.impl.tookit.Objects;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

import java.util.List;

public class InsertTable implements IInsertTable<Table> {

    protected final Table table;

    public InsertTable(Table table) {
        this.table = table;
    }

    @Override
    public Table getTable() {
        return table;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        AbstractInsert abstractInsert = (AbstractInsert) parent;
        if (context.getDbType() == DbType.ORACLE && parent instanceof AbstractInsert) {

            List<List<Cmd>> insertValuesList = null;
            if (Objects.nonNull(abstractInsert.getInsertValues())) {
                insertValuesList = abstractInsert.getInsertValues().getValues();
            }
            if (Objects.nonNull(insertValuesList) && insertValuesList.size() > 1) {
                sqlBuilder.append(" INSERT ALL ");
                return sqlBuilder;
            }
        }

        boolean insertIgnore = (context.getDbType() == DbType.MYSQL || context.getDbType() == DbType.MARIA_DB || context.getDbType() == DbType.H2)
                && abstractInsert.getConflictAction() != null
                && abstractInsert.getConflictAction().getConflictKeys() == null
                && abstractInsert.getConflictAction().getUpdateSets() == null;

        sqlBuilder.append(insertIgnore ? SqlConst.INSERT_IGNORE_INTO : SqlConst.INSERT_INTO);
        sqlBuilder.append(this.table.getName());
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.table);
    }
}
