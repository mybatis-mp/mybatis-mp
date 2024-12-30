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

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityBatchInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.SaveBatchStrategy;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import db.sql.api.Getter;
import db.sql.api.tookit.LambdaUtil;

import java.util.Collection;
import java.util.Objects;

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

    public static <E> int saveBatch(BasicMapper basicMapper, BaseInsert<?> insert, TableInfo tableInfo, Collection<E> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        SaveBatchStrategy saveBatchStrategy = new SaveBatchStrategy();
        return saveBatch(basicMapper, insert, tableInfo, list, saveBatchStrategy);
    }

    public static <E> int saveBatch(BasicMapper basicMapper, BaseInsert<?> insert, TableInfo tableInfo, Collection<E> list, Getter<E>[] forceFields) {
        if (Objects.isNull(forceFields) || forceFields.length < 1) {
            throw new RuntimeException("forceFields can't be null or empty");
        }
        SaveBatchStrategy saveBatchStrategy = new SaveBatchStrategy();
        saveBatchStrategy.forceFields(forceFields);
        return saveBatch(basicMapper, insert, tableInfo, list, saveBatchStrategy);
    }

    public static <E> int saveBatch(BasicMapper basicMapper, BaseInsert<?> insert, TableInfo tableInfo, Collection<E> list, SaveBatchStrategy<E> saveBatchStrategy) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        return basicMapper.$save(new EntityBatchInsertContext(insert, tableInfo, list, saveBatchStrategy));
    }
}
