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

import cn.mybatis.mp.core.db.reflect.ModelFieldInfo;
import cn.mybatis.mp.core.db.reflect.ModelInfo;
import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelBatchInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelInsertContext;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.impl.tookit.LambdaUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class SaveModelMethodUtil {

    public static <M extends Model> int save(BasicMapper basicMapper, M model, boolean allFieldForce, Getter<M>[] forceFields) {
        return basicMapper.$saveModel(new ModelInsertContext(model, allFieldForce, LambdaUtil.getFieldNames(forceFields)));
    }

    public static <M extends Model> int save(BasicMapper basicMapper, Collection<M> list, boolean allFieldForce, Getter<M>[] forceFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }

        int cnt = 0;
        for (M model : list) {
            cnt += save(basicMapper, model, allFieldForce, forceFields);
        }
        return cnt;
    }

    public static <M extends Model> int saveBatch(BasicMapper basicMapper, Collection<M> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> saveFieldSet = new HashSet<>();
        DbType dbType = basicMapper.getCurrentDbType();
        M first = list.stream().findFirst().get();
        ModelInfo modelInfo = Models.get(first.getClass());
        for (ModelFieldInfo modelFieldInfo : modelInfo.getModelFieldInfos()) {
            if (modelFieldInfo.getTableFieldInfo().isTableId()) {
                TableId tableId = TableInfoUtil.getTableIdAnnotation(modelFieldInfo.getTableFieldInfo().getField(), dbType);
                Objects.requireNonNull(tableId.value());
                if (tableId.value() == IdAutoType.AUTO) {
                    Object id;
                    try {
                        id = modelFieldInfo.getReadFieldInvoker().invoke(first, null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (Objects.isNull(id)) {
                        continue;
                    }
                }
            }
            saveFieldSet.add(modelFieldInfo.getField().getName());
        }
        return basicMapper.$save(new ModelBatchInsertContext(modelInfo, list, saveFieldSet));
    }

    public static <M extends Model> int saveBatch(BasicMapper basicMapper, Collection<M> list, Getter<M>... forceFields) {
        if (Objects.isNull(forceFields) || forceFields.length < 1) {
            throw new RuntimeException("forceFields can't be null or empty");
        }
        M first = list.stream().findFirst().get();
        ModelInfo modelInfo = Models.get(first.getClass());
        return basicMapper.$save(new ModelBatchInsertContext(modelInfo, list, LambdaUtil.getFieldNames(forceFields)));
    }
}
