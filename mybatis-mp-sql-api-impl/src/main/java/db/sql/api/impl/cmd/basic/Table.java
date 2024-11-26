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
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.impl.cmd.struct.From;
import db.sql.api.impl.cmd.struct.Join;
import db.sql.api.impl.tookit.LambdaUtil;
import db.sql.api.impl.tookit.SqlConst;

import java.util.Objects;

public class Table implements ITable<Table, TableField>, IDataset<Table, TableField> {

    protected String alias;

    protected String prefix;

    protected String name;

    protected String forceIndex;

    public Table(String name) {
        this.name = name;
    }

    public Table(String name, String alias) {
        this(name);
        this.alias = alias;
    }

    @Override
    public TableField $(String name) {
        return new TableField(this, name);
    }


    public String getName() {
        return name;
    }

    public Table setName(String name) {
        this.name = name;
        return this;
    }

    public String getName(DbType dbType) {
        return dbType.wrap(this.name);
    }

    @Override
    public String getAlias() {
        return alias;
    }

    public Table setAlias(String alias) {
        return as(alias);
    }

    public Table as(String alias) {
        this.alias = alias;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public Table setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        sqlBuilder.append(getName(context.getDbType()));
        if (getAlias() != null) {
            sqlBuilder.append(SqlConst.BLANK).append(getAlias());
        }
        if (parent instanceof From || parent instanceof Join) {
            if (Objects.nonNull(forceIndex) && !SqlConst.S_EMPTY.equals(forceIndex)) {
                sqlBuilder.append(SqlConst.FORCE_INDEX(context.getDbType(), forceIndex));
            }
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return false;
    }

    public Table forceIndex(String forceIndex) {
        this.forceIndex = forceIndex;
        return this;
    }

    @Override
    public <E> TableField $(Getter<E> column) {
        return this.$(LambdaUtil.getName(column));
    }
}
