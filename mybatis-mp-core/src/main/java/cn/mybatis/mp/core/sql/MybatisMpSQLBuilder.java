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

package cn.mybatis.mp.core.sql;

import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.tookit.OptimizeOptions;
import db.sql.api.impl.tookit.SQLOptimizeUtils;

public class MybatisMpSQLBuilder implements SQLBuilder {

    /**
     * 构建query sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    @Override
    public StringBuilder buildQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions) {
        return SQLOptimizeUtils.getOptimizedSql(query, context, optimizeOptions);
    }

    /**
     * 构建count查询sql
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    @Override
    public StringBuilder buildCountQuerySQl(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions) {
        return SQLOptimizeUtils.getOptimizedCountSql(query, context, optimizeOptions);
    }

    /**
     * 从query中构建count sql，一般用于分页时使用
     *
     * @param query           查询
     * @param context         上下文
     * @param optimizeOptions 优化配置
     * @return
     */
    @Override
    public StringBuilder buildCountSQLFromQuery(BaseQuery query, SqlBuilderContext context, OptimizeOptions optimizeOptions) {
        return SQLOptimizeUtils.getCountSqlFromQuery(query, context, optimizeOptions);
    }

    @Override
    public StringBuilder buildInsertSQL(BaseInsert insert, SqlBuilderContext context) {
        return insert.sql(context, new StringBuilder(SQLOptimizeUtils.getStringBuilderCapacity(insert.cmds())));
    }

    @Override
    public StringBuilder buildUpdateSQL(BaseUpdate update, SqlBuilderContext context) {
        return update.sql(context, new StringBuilder(SQLOptimizeUtils.getStringBuilderCapacity(update.cmds())));
    }

    @Override
    public StringBuilder buildDeleteSQL(BaseDelete delete, SqlBuilderContext context) {
        return delete.sql(context, new StringBuilder(SQLOptimizeUtils.getStringBuilderCapacity(delete.cmds())));
    }
}
