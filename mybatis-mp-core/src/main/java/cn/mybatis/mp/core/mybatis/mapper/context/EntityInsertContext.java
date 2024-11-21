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

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableIds;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.incrementer.IdentifierGenerator;
import cn.mybatis.mp.core.incrementer.IdentifierGeneratorFactory;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.Insert;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.core.util.TypeConvertUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.TableField;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.NULL;
import db.sql.api.impl.cmd.basic.Table;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EntityInsertContext<T> extends SQLCmdInsertContext<BaseInsert> implements SetIdMethod {

    private final T entity;

    private final TableInfo tableInfo;

    private final boolean allFieldForce;

    private final Set<String> forceFields;

    private final boolean idHasValue;

    public EntityInsertContext(TableInfo tableInfo, T entity, boolean allFieldForce, Set<String> forceFields) {
        this.entity = entity;
        this.allFieldForce = allFieldForce;
        this.entityType = entity.getClass();
        this.tableInfo = tableInfo;
        this.forceFields = forceFields;
        this.idHasValue = IdUtil.isIdExists(entity, tableInfo.getIdFieldInfo());
    }

    @Override
    public void init(DbType dbType) {
        super.init(dbType);
        if (Objects.isNull(this.execution)) {
            this.execution = createCmd(dbType);
        }
    }

    private Insert createCmd(DbType dbType) {
        //设置租户ID
        TenantUtil.setTenantId(this.tableInfo, this.entity);

        Insert insert = new Insert();
        MybatisCmdFactory $ = insert.$();
        Table table = $.table(tableInfo.getSchemaAndTableName());
        insert.insert(table);
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < tableInfo.getFieldSize(); i++) {
            TableFieldInfo tableFieldInfo = tableInfo.getTableFieldInfos().get(i);

            if (!tableFieldInfo.getTableFieldAnnotation().insert()) {
                continue;
            }

            boolean isNeedInsert = false;
            Object value = tableFieldInfo.getValue(entity);
            if (tableFieldInfo.isTableId()) {
                if (!IdUtil.isIdValueExists(value)) {
                    TableId tableId = TableIds.get(entity.getClass(), dbType);
                    if (tableId.value() == IdAutoType.GENERATOR) {
                        isNeedInsert = true;
                        IdentifierGenerator identifierGenerator = IdentifierGeneratorFactory.getIdentifierGenerator(tableId.generatorName());
                        Object id = identifierGenerator.nextId(tableInfo.getType());
                        if (IdUtil.setId(entity, tableFieldInfo, id)) {
                            value = id;
                        }
                    }
                } else {
                    isNeedInsert = true;
                }
            } else if (Objects.nonNull(value)) {
                isNeedInsert = true;
            } else if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().defaultValue())) {
                isNeedInsert = true;

                //设置默认值
                value = MybatisMpConfig.getDefaultValue(tableFieldInfo.getFieldInfo().getTypeClass(), tableFieldInfo.getTableFieldAnnotation().defaultValue());
                //默认值回写
                TableInfoUtil.setValue(tableFieldInfo, entity, value);
            } else if (tableFieldInfo.isVersion()) {
                isNeedInsert = true;

                //乐观锁设置 默认值1
                value = TypeConvertUtil.convert(Integer.valueOf(1), tableFieldInfo.getField().getType());
                //乐观锁回写
                TableInfoUtil.setValue(tableFieldInfo, entity, value);
            } else if (allFieldForce || (Objects.nonNull(this.forceFields) && this.forceFields.contains(tableFieldInfo.getField().getName()))) {
                isNeedInsert = true;
            }

            if (isNeedInsert) {
                insert.fields($.field(table, tableFieldInfo.getColumnName()));
                if (Objects.isNull(value)) {
                    values.add(NULL.NULL);
                } else {
                    TableField tableField = tableFieldInfo.getTableFieldAnnotation();
                    MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                    values.add(Methods.value(mybatisParameter));
                }
            }
        }
        insert.values(values);

        return insert;
    }

    @Override
    public void setId(Object id, int index) {
        IdUtil.setId(this.entity, this.tableInfo.getSingleIdFieldInfo(true), id);
    }

    @Override
    public boolean idHasValue() {
        return idHasValue;
    }

    @Override
    public int getInsertSize() {
        return 1;
    }

    @Override
    public Object getInsertObject(int index) {
        return this.entity;
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
