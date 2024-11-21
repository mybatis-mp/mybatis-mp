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

import java.util.*;
import java.util.stream.Collectors;

public class EntityBatchInsertContext<T> extends SQLCmdInsertContext<BaseInsert> implements SetIdMethod {

    private final T[] insertDatas;

    private final Set<String> saveFieldSet;

    private final TableInfo tableInfo;

    private final boolean idHasValue;

    public EntityBatchInsertContext(TableInfo tableInfo, Collection<T> list, Set<String> saveFieldSet) {
        this.tableInfo = tableInfo;
        this.insertDatas = list.toArray((T[]) new Object[0]);
        this.saveFieldSet = saveFieldSet;
        this.entityType = tableInfo.getType();
        this.idHasValue = IdUtil.isIdExists(this.insertDatas[0], tableInfo.getIdFieldInfo());
    }

    private static Insert createCmd(TableInfo tableInfo, Object[] array, Set<String> saveFieldSet, DbType dbType) {
        Insert insert = new Insert();
        Class<?> entityType = tableInfo.getType();
        insert.$().cacheTableInfo(tableInfo);
        Table table = insert.$().table(tableInfo.getSchemaAndTableName());
        insert.insert(table);


        List<TableFieldInfo> saveFieldInfoSet = saveFieldSet.stream().map(tableInfo::getFieldInfo).collect(Collectors.toList());

        TableId tableId = null;

        //拼上主键
        if (!tableInfo.getIdFieldInfos().isEmpty()) {
            tableId = TableIds.get(entityType, dbType);
            if (tableId.value() == IdAutoType.GENERATOR) {
                tableInfo.getIdFieldInfos().forEach(item -> {
                    if (!saveFieldInfoSet.contains(item)) {
                        saveFieldInfoSet.add(item);
                    }
                });
            }
        }

        //拼上租户ID
        if (Objects.nonNull(tableInfo.getTenantIdFieldInfo())) {
            if (!saveFieldInfoSet.contains(tableInfo.getTenantIdFieldInfo())) {
                saveFieldInfoSet.add(tableInfo.getTenantIdFieldInfo());
            }
        }

        //拼上乐观锁
        if (Objects.nonNull(tableInfo.getVersionFieldInfo())) {
            if (!saveFieldInfoSet.contains(tableInfo.getVersionFieldInfo())) {
                saveFieldInfoSet.add(tableInfo.getVersionFieldInfo());
            }
        }

        //拼上逻辑删除
        if (Objects.nonNull(tableInfo.getLogicDeleteFieldInfo())) {
            if (!saveFieldInfoSet.contains(tableInfo.getLogicDeleteFieldInfo())) {
                saveFieldInfoSet.add(tableInfo.getLogicDeleteFieldInfo());
            }
        }

        //设置insert 列
        for (TableFieldInfo tableFieldInfo : saveFieldInfoSet) {
            insert.fields(insert.$().field(table, tableFieldInfo.getColumnName()));
        }

        int fieldSize = saveFieldInfoSet.size();

        boolean containId = false;
        for (Object t : array) {
            List<Object> values = new ArrayList<>();
            for (int i = 0; i < fieldSize; i++) {
                TableFieldInfo tableFieldInfo = saveFieldInfoSet.get(i);
                Object value = tableFieldInfo.getValue(t);
                boolean hasValue = (!tableFieldInfo.isTableId() && Objects.nonNull(value)) || (tableFieldInfo.isTableId() && IdUtil.isIdValueExists(value));
                if (!hasValue) {
                    if (tableFieldInfo.isTableId()) {
                        if (tableId.value() == IdAutoType.GENERATOR) {
                            IdentifierGenerator identifierGenerator = IdentifierGeneratorFactory.getIdentifierGenerator(tableId.generatorName());
                            Object id = identifierGenerator.nextId(tableInfo.getType());
                            if (IdUtil.setId(t, tableFieldInfo, id)) {
                                value = id;
                            }
                        } else {
                            throw new RuntimeException(tableFieldInfo.getField().getName() + " has no value");
                        }
                    } else if (tableFieldInfo.isTenantId()) {
                        value = TenantUtil.setTenantId(t);
                    } else if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().defaultValue())) {
                        //设置默认值
                        value = MybatisMpConfig.getDefaultValue(tableFieldInfo.getFieldInfo().getTypeClass(), tableFieldInfo.getTableFieldAnnotation().defaultValue());

                        //默认值回写
                        TableInfoUtil.setValue(tableFieldInfo, t, value);
                    } else if (tableFieldInfo.isVersion()) {
                        //乐观锁设置 默认值1
                        value = TypeConvertUtil.convert(Integer.valueOf(1), tableFieldInfo.getField().getType());
                        //乐观锁回写
                        TableInfoUtil.setValue(tableFieldInfo, t, value);
                    }
                }

                if (tableFieldInfo.isTableId()) {
                    containId = true;
                }

                TableField tableField = tableFieldInfo.getTableFieldAnnotation();
                if (Objects.isNull(value)) {
                    values.add(NULL.NULL);
                } else {
                    MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                    values.add(Methods.value(mybatisParameter));
                }
            }
            insert.values(values);
        }

        if (dbType == DbType.SQL_SERVER && insert.getInsertValues().getValues().size() > 0) {
            if (!containId && Objects.nonNull(tableId) && tableId.value() == IdAutoType.AUTO) {
                insert.getInsertFields().setOutput("OUTPUT INSERTED." + tableInfo.getIdFieldInfo().getColumnName());
            }
        }

        return insert;
    }

    @Override
    public void init(DbType dbType) {
        super.init(dbType);
        if (Objects.isNull(this.execution)) {
            this.execution = createCmd(this.tableInfo, this.insertDatas, this.saveFieldSet, dbType);
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
