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

package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import db.sql.api.DbType;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

import java.util.Collection;
import java.util.Objects;

public class EntityBatchInsertContext<T> extends SQLCmdInsertContext<BaseInsert> implements SetIdMethod {

    private final T[] insertDatas;

    private final SaveBatchStrategy<T> saveBatchStrategy;

    private final TableInfo tableInfo;

    private final boolean idHasValue;

    private final BaseInsert<?> insert;

    public EntityBatchInsertContext(BaseInsert<?> insert, TableInfo tableInfo, Collection<T> list, SaveBatchStrategy<T> saveBatchStrategy) {
        this.insert = insert;
        this.tableInfo = tableInfo;
        this.insertDatas = list.toArray((T[]) new Object[0]);
        this.saveBatchStrategy = saveBatchStrategy;
        this.entityType = tableInfo.getType();
        this.idHasValue = IdUtil.isIdExists(this.insertDatas[0], tableInfo.getIdFieldInfo());
    }


    @Override
    public void init(DbType dbType) {
        super.init(dbType);
        if (Objects.isNull(this.execution)) {
            this.execution = EntityBatchInsertCreateUtil.create(insert, this.tableInfo, this.insertDatas, saveBatchStrategy, dbType, useBatchExecutor);
        }
    }

    @Override
    public void setId(Object id, int index) {
        IdUtil.setId(this.insertDatas[index], this.tableInfo.getSingleIdFieldInfo(true), id);
    }

    @Override
    public boolean idHasValue() {
        return idHasValue;
    }

    @Override
    public int getInsertSize() {
        return this.insertDatas.length;
    }

    @Override
    public Object getInsertObject(int index) {
        return this.insertDatas[index];
    }

    @Override
    public TypeHandler<?> getIdTypeHandler(Configuration configuration) {
        if (Objects.nonNull(this.tableInfo.getIdFieldInfo())) {
            TypeHandler typeHandler = this.tableInfo.getIdFieldInfo().getTypeHandler();
            if (Objects.isNull(typeHandler)) {
                return configuration.getTypeHandlerRegistry().getTypeHandler(this.tableInfo.getIdFieldInfo().getFieldInfo().getTypeClass());
            }
        }
        return null;
    }

    @Override
    public String getIdColumnName() {
        return this.tableInfo.getIdFieldInfo().getColumnName();
    }
}
