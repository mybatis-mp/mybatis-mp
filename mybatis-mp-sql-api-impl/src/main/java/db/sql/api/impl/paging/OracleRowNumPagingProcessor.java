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
import db.sql.api.impl.cmd.condition.In;
import db.sql.api.impl.cmd.struct.Limit;
import db.sql.api.impl.cmd.struct.update.UpdateSet;

import java.util.List;

public class OracleRowNumPagingProcessor implements IPagingProcessor {

    @Override
    public StringBuilder buildPagingSQL(SqlBuilderContext sqlBuilderContext, Cmd parent, IQuery query, StringBuilder sql, Limit limit) {
        String alias = null;
        if (query instanceof Alias) {
            alias = ((Alias) query).getAlias();
        }
        if (alias == null || alias.isEmpty()) {
            alias = "NT";
        }
        String rnName = OracleRowNumNameUtil.getRowName(sqlBuilderContext);
        StringBuilder newSql = new StringBuilder("SELECT ");

        boolean handlerSelect = false;
        if (parent != null && (parent instanceof In || parent instanceof UpdateSet)) {
            //假如是在in条件里
            List<Cmd> selectFields = query.getSelect().getSelectField();
            if (selectFields.size() == 1) {
                Cmd cmd = selectFields.get(0);
                if (cmd instanceof IDatasetField) {
                    IDatasetField a = (IDatasetField) cmd;
                    if (a.getAlias() != null) {
                        newSql.append(a.getAlias());
                    } else {
                        newSql.append(a.getName(sqlBuilderContext.getDbType()));
                    }
                    handlerSelect = true;
                } else if (cmd instanceof Alias) {
                    Alias a = (Alias) cmd;
                    if (a.getAlias() != null) {
                        //假如是在in条件里
                        newSql.append(a.getAlias());
                        handlerSelect = true;
                    }
                }
            }
        }

        if (!handlerSelect) {
            newSql.append("*");
        }

        return newSql.append("  FROM ( SELECT IT.*,ROWNUM ").append(rnName).append(" FROM (")
                .append(sql).append(") IT WHERE ROWNUM <= ")
                .append(limit.getLimit() + limit.getOffset())
                .append(") ").append(alias).append(" WHERE ").append(alias).append(".").append(rnName).append(" >").append(limit.getOffset());
    }
}
