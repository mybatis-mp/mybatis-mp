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
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.basic.Table;

import java.util.Collection;
import java.util.Objects;

public class SaveOrUpdateModelMethodUtil {

    public static <M extends Model> int saveOrUpdate(BasicMapper basicMapper, ModelInfo modelInfo, M model, boolean allFieldForce, Getter<M>[] forceFields) {
        if (modelInfo.getIdFieldInfos().isEmpty()) {
            throw new RuntimeException(modelInfo.getType().getName() + " has no id");
        }
        Object id;
        try {
            id = modelInfo.getIdFieldInfos().get(0).getReadFieldInvoker().invoke(model, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (Objects.isNull(id)) {
            return SaveModelMethodUtil.save(basicMapper, model, allFieldForce, forceFields);
        }

        Query<?> query = Query.create();
        query.$().cacheTableInfo(modelInfo.getTableInfo());
        Table table = query.$(modelInfo.getTableInfo().getType());
        query.select1().from(table);

        WhereUtil.appendIdWhereWithModel(query.$where(), modelInfo, model);
        boolean exists = basicMapper.exists(query);
        if (exists) {
            return UpdateModelMethodUtil.update(basicMapper, model, allFieldForce, forceFields);
        } else {
            return SaveModelMethodUtil.save(basicMapper, model, allFieldForce, forceFields);
        }
    }

    public static <M extends Model> int saveOrUpdate(BasicMapper basicMapper, Collection<M> list, boolean allFieldForce, Getter<M>[] forceFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        M first = list.stream().findFirst().get();
        ModelInfo modelInfo = Models.get(first.getClass());
        int cnt = 0;
        for (M model : list) {
            cnt += saveOrUpdate(basicMapper, modelInfo, model, allFieldForce, forceFields);
        }
        return cnt;
    }
}
