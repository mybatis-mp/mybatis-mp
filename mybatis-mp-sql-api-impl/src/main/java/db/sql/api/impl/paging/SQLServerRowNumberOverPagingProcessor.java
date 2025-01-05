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

package db.sql.api.impl.paging;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.Alias;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.cmd.struct.query.ISelect;
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.struct.Limit;
import db.sql.api.impl.cmd.struct.query.Select;
import db.sql.api.impl.cmd.struct.update.UpdateSet;

import java.util.List;

public class SQLServerRowNumberOverPagingProcessor implements IPagingProcessor {

    @Override
    public StringBuilder buildPagingSQL(SqlBuilderContext sqlBuilderContext, Cmd parent, IQuery query, StringBuilder sql, Limit limit) {
        String alias = null;
        if (query instanceof Alias) {
            alias = ((Alias) query).getAlias();
        }
        if (alias == null || alias.isEmpty()) {
            alias = "NT";
        }

        ISelect select=query.getSelect();
        String rnName = OracleRowNumNameUtil.getRowName(sqlBuilderContext);
        StringBuilder orderBy = query.getOrderBy().sql(null,parent,sqlBuilderContext,new StringBuilder());
        StringBuilder newSql = new StringBuilder("SELECT TOP ").append(limit.getLimit());
        newSql.append(" *  FROM (");
        select.sql(null,parent,sqlBuilderContext,newSql);
        newSql.append(",ROW_NUMBER() OVER(").append(orderBy).append(") ").append(rnName);
        newSql.append(sql).append(") ").append(alias);
        newSql.append(" WHERE ").append(rnName).append(" > ").append(limit.getOffset());
        return newSql;
    }
}
