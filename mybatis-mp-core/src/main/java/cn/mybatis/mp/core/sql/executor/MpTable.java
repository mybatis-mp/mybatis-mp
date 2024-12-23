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

package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.util.TableInfoUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.tookit.LambdaUtil;

public class MpTable extends db.sql.api.impl.cmd.basic.Table {

    protected final TableInfo tableInfo;

    public MpTable(TableInfo tableInfo) {
        super(tableInfo.getSchemaAndTableName());
        this.tableInfo = tableInfo;
    }

    public MpTable(TableInfo tableInfo, String alias) {
        this(tableInfo);
        this.alias = alias;
    }

    @Override
    public <E> TableField $(Getter<E> column) {
        LambdaUtil.LambdaFieldInfo fieldInfo = LambdaUtil.getFieldInfo(column);
        if (fieldInfo.getType() == tableInfo.getType()) {
            return new MpTableField(this, tableInfo.getFieldInfo(fieldInfo.getName()));
        }
        return super.$(TableInfoUtil.getColumnName(column));
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }
}
