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

import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableIds;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.incrementer.IdentifierGenerator;
import cn.mybatis.mp.core.incrementer.IdentifierGeneratorFactory;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.Insert;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.DefaultValueUtil;
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

import java.util.*;
import java.util.stream.Collectors;

public class EntityBatchInsertCreateUtil {

    private static Set<String> getAllSaveField(TableInfo tableInfo, DbType dbType, Object entity) {
        Set<String> saveFieldSet = new HashSet<>();
        for (TableFieldInfo tableFieldInfo : tableInfo.getTableFieldInfos()) {
            if (!tableFieldInfo.getTableFieldAnnotation().insert()) {
                continue;
            }
            if (tableFieldInfo.isTableId()) {
                TableId tableId = TableInfoUtil.getTableIdAnnotation(tableFieldInfo.getField(), dbType);
                Objects.requireNonNull(tableId.value());
                if (tableId.value() == IdAutoType.AUTO) {
                    Object id;
                    try {
                        id = tableFieldInfo.getReadFieldInvoker().invoke(entity, null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (Objects.isNull(id)) {
                        continue;
                    }
                }
            }
            saveFieldSet.add(tableFieldInfo.getField().getName());
        }
        return saveFieldSet;
    }


    public static <T> BaseInsert<?> create(BaseInsert<?> insert, TableInfo tableInfo, T[] array, SaveBatchStrategy<T> saveBatchStrategy, DbType dbType, boolean useBatchExecutor) {

        insert = insert == null ? new Insert() : insert;

        insert.$().cacheTableInfo(tableInfo);
        Table table = insert.$().table(tableInfo.getType());
        insert.insert(table);

        Set<String> saveFieldSet;
        if (saveBatchStrategy.getForceFields() == null || saveBatchStrategy.getForceFields().isEmpty()) {
            saveFieldSet = getAllSaveField(tableInfo, dbType, array[0]);
        } else {
            saveFieldSet = saveBatchStrategy.getForceFields();
        }

        List<TableFieldInfo> saveFieldInfoSet = saveFieldSet.stream().map(tableInfo::getFieldInfo).collect(Collectors.toList());

        //拼上主键
        if (!tableInfo.getIdFieldInfos().isEmpty()) {
            tableInfo.getIdFieldInfos().forEach(idFieldInfo -> {
                TableId tableId = TableInfoUtil.getTableIdAnnotation(idFieldInfo.getField(), dbType);
                if (tableId.value() == IdAutoType.GENERATOR || tableId.value() == IdAutoType.SQL) {
                    if (!saveFieldInfoSet.contains(idFieldInfo)) {
                        saveFieldInfoSet.add(idFieldInfo);
                    }
                }
            });
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
                        TableId tableId = TableInfoUtil.getTableIdAnnotation(tableFieldInfo.getField(), dbType);
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
                    } else if (tableFieldInfo.isLogicDelete()) {
                        //逻辑删除字段
                        //设置删除初始值
                        value = tableFieldInfo.getLogicDeleteInitValue();
                        if (value != null) {
                            //逻辑删除初始值回写
                            TableInfoUtil.setValue(tableFieldInfo, t, value);
                        } else if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().defaultValue())) {
                            //读取回填 @TableField里的默认值
                            value = DefaultValueUtil.getAndSetDefaultValue(t, tableFieldInfo);
                        }
                    } else if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().defaultValue())) {
                        //读取回填 默认值
                        value = DefaultValueUtil.getAndSetDefaultValue(t, tableFieldInfo);
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
            TableId tableId = TableIds.get(tableInfo.getType(), dbType);
            if (!useBatchExecutor && !containId && Objects.nonNull(tableId) && tableId.value() == IdAutoType.AUTO) {
                insert.getInsertFields().setOutput("OUTPUT INSERTED." + tableInfo.getIdFieldInfo().getColumnName());
            }
        }
        if (saveBatchStrategy.getBeforeListener() != null) {
            saveBatchStrategy.getBeforeListener().accept(insert);
        }
        return insert;
    }
}
