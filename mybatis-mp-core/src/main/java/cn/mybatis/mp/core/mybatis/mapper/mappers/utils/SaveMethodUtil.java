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

package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityBatchInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityInsertContext;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.tookit.LambdaUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class SaveMethodUtil {

    public static <T> int save(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Getter<T>[] forceFields) {
        return basicMapper.$saveEntity(new EntityInsertContext(tableInfo, entity, allFieldForce, LambdaUtil.getFieldNames(forceFields)));
    }

    public static <T> int save(BasicMapper basicMapper, TableInfo tableInfo, Collection<T> list, boolean allFieldForce, Getter<T>[] forceFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }

        int cnt = 0;
        for (T entity : list) {
            cnt += save(basicMapper, tableInfo, entity, allFieldForce, forceFields);
        }
        return cnt;
    }

    public static <E> int saveBatch(BasicMapper basicMapper, TableInfo tableInfo, Collection<E> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> saveFieldSet = new HashSet<>();
        DbType dbType = basicMapper.getCurrentDbType();
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
                        id = tableFieldInfo.getReadFieldInvoker().invoke(list.stream().findFirst().get(), null);
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
        return basicMapper.$save(new EntityBatchInsertContext(tableInfo, list, saveFieldSet));
    }

    public static <E> int saveBatch(BasicMapper basicMapper, TableInfo tableInfo, Collection<E> list, Getter<E>... forceFields) {
        if (Objects.isNull(forceFields) || forceFields.length < 1) {
            throw new RuntimeException("forceFields can't be null or empty");
        }
        return basicMapper.$save(new EntityBatchInsertContext(tableInfo, list, LambdaUtil.getFieldNames(forceFields)));
    }
}
