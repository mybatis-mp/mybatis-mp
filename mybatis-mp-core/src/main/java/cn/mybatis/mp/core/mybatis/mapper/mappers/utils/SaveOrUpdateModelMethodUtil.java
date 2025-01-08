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
import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveOrUpdateStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.UpdateStrategy;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.core.util.ModelInfoUtil;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Collection;
import java.util.Objects;

public class SaveOrUpdateModelMethodUtil {

    public static <M extends Model<T>, T> int saveOrUpdate(BasicMapper basicMapper, M model, SaveOrUpdateStrategy saveOrUpdateStrategy) {
        return saveOrUpdate(basicMapper, Models.get(model.getClass()), model, saveOrUpdateStrategy);
    }

    public static <M extends Model<T>, T> int saveOrUpdate(BasicMapper basicMapper, ModelInfo modelInfo, M model, SaveOrUpdateStrategy saveOrUpdateStrategy) {
        boolean checkById = true;
        if (saveOrUpdateStrategy.getOn() != null) {
            checkById = false;
        }

        Where checkWhere = WhereUtil.create(modelInfo.getTableInfo());
        if (checkById) {
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
                SaveStrategy<M> saveStrategy = new SaveStrategy<>()
                        .allFieldSave(saveOrUpdateStrategy.isAllField())
                        .forceFields(saveOrUpdateStrategy.getForceFields());
                return SaveModelMethodUtil.save(basicMapper, model, saveStrategy);
            }
            //使用主键查询
            WhereUtil.appendIdWhereWithModel(checkWhere, modelInfo, model);
        } else {
            saveOrUpdateStrategy.getOn().accept(checkWhere);
        }

        Query<T> query = new Query<>(checkWhere);
        query.$().cacheTableInfo(modelInfo.getTableInfo());
        Table table = query.$(modelInfo.getEntityType());

        if (saveOrUpdateStrategy.isIgnoreLogicDeleteWhenCheck()) {
            LogicDeleteUtil.execute(false, () -> {
                query.from(table).returnType(modelInfo.getEntityType());
            });
        } else {
            query.from(table).returnType(modelInfo.getEntityType());
        }


        for (String c : modelInfo.getTableInfo().getIdColumnNames()) {
            query.select(table.$(c));
        }

        T obj = basicMapper.get(query);
        if (obj == null) {
            SaveStrategy<M> saveStrategy = new SaveStrategy<>()
                    .allFieldSave(saveOrUpdateStrategy.isAllField())
                    .forceFields(saveOrUpdateStrategy.getForceFields());
            return SaveModelMethodUtil.save(basicMapper, model, saveStrategy);
        } else {
            modelInfo.getIdFieldInfos().stream().forEach(item -> {
                ModelInfoUtil.setValue(item, model, TableInfoUtil.getEntityFieldValue(item.getTableFieldInfo(), obj));
            });
            UpdateStrategy updateStrategy = UpdateMethodUtil.createUpdateStrategy();
            updateStrategy.allFieldUpdate(saveOrUpdateStrategy.isAllField());
            updateStrategy.forceFields(saveOrUpdateStrategy.getForceFields());
            return UpdateModelMethodUtil.update(basicMapper, model, updateStrategy);
        }
    }

    public static <M extends Model> int saveOrUpdate(BasicMapper basicMapper, Collection<M> list, SaveOrUpdateStrategy saveOrUpdateStrategy) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        M first = list.stream().findFirst().get();
        ModelInfo modelInfo = Models.get(first.getClass());
        int cnt = 0;
        for (M model : list) {
            cnt += saveOrUpdate(basicMapper, modelInfo, model, saveOrUpdateStrategy);
        }
        return cnt;
    }
}
