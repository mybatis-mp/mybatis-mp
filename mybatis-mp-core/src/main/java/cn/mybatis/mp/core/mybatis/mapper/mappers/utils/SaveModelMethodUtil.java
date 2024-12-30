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

import cn.mybatis.mp.core.db.reflect.ModelInfo;
import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelBatchInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.SaveBatchStrategy;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.tookit.LambdaUtil;

import java.util.Collection;
import java.util.Objects;

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

    public static <M extends Model> int saveBatch(BasicMapper basicMapper, BaseInsert<?> insert, Collection<M> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        SaveBatchStrategy saveBatchStrategy = new SaveBatchStrategy();
        return saveBatch(basicMapper, insert, list, saveBatchStrategy);
    }

    public static <M extends Model> int saveBatch(BasicMapper basicMapper, BaseInsert<?> insert, Collection<M> list, Getter<M>[] forceFields) {
        if (Objects.isNull(forceFields) || forceFields.length < 1) {
            throw new RuntimeException("forceFields can't be null or empty");
        }

        SaveBatchStrategy saveBatchStrategy = new SaveBatchStrategy();
        saveBatchStrategy.forceFields(forceFields);
        return saveBatch(basicMapper, insert, list, saveBatchStrategy);
    }

    public static <M extends Model> int saveBatch(BasicMapper basicMapper, BaseInsert<?> insert, Collection<M> list, SaveBatchStrategy<M> saveBatchStrategy) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        M first = list.stream().findFirst().get();
        ModelInfo modelInfo = Models.get(first.getClass());
        return basicMapper.$save(new ModelBatchInsertContext<>(insert, modelInfo, list, saveBatchStrategy));
    }
}
