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
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveOrUpdateStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.UpdateStrategy;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.core.util.TableInfoUtil;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Collection;
import java.util.Objects;

public class SaveOrUpdateMethodUtil {

    public static <T> int saveOrUpdate(BasicMapper basicMapper, TableInfo tableInfo, T entity, SaveOrUpdateStrategy saveOrUpdateStrategy) {
        Class<?> entityType = entity.getClass();

        boolean checkById = true;
        if (saveOrUpdateStrategy.getOn() != null) {
            checkById = false;
        }

        Where checkWhere = WhereUtil.create(tableInfo);
        if (checkById) {
            if (tableInfo.getIdFieldInfos().isEmpty()) {
                throw new RuntimeException(entityType.getName() + " has no id");
            }
            Object id;
            try {
                id = tableInfo.getIdFieldInfos().get(0).getReadFieldInvoker().invoke(entity, null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            if (Objects.isNull(id)) {
                SaveStrategy<T> saveStrategy = new SaveStrategy<>()
                        .allFieldSave(saveOrUpdateStrategy.isAllField())
                        .forceFields(saveOrUpdateStrategy.getForceFields());
                return SaveMethodUtil.save(basicMapper, tableInfo, entity, saveStrategy);
            }
            //使用主键查询
            WhereUtil.appendIdWhereWithEntity(checkWhere, tableInfo, entity);
        } else {
            saveOrUpdateStrategy.getOn().accept(checkWhere);
        }

        Query<T> query = new Query<>(checkWhere);
        query.$().cacheTableInfo(tableInfo);
        Table table = query.$(entityType);
        query.from(table).returnType(entityType);

        for (String c : tableInfo.getIdColumnNames()) {
            query.select(table.$(c));
        }

        T obj = basicMapper.get(query);
        if (obj == null) {
            SaveStrategy<T> saveStrategy = new SaveStrategy<>()
                    .allFieldSave(saveOrUpdateStrategy.isAllField())
                    .forceFields(saveOrUpdateStrategy.getForceFields());
            return SaveMethodUtil.save(basicMapper, tableInfo, entity, saveStrategy);
        } else {
            tableInfo.getIdFieldInfos().stream().forEach(item -> {
                TableInfoUtil.setValue(item, entity, TableInfoUtil.getEntityFieldValue(item, obj));
            });
            UpdateStrategy<T> updateStrategy = new UpdateStrategy<>();
            updateStrategy
                    .allFieldUpdate(saveOrUpdateStrategy.isAllField())
                    .forceFields(saveOrUpdateStrategy.getForceFields());
            return UpdateMethodUtil.update(basicMapper, tableInfo, entity, updateStrategy);
        }
    }

    public static <T> int saveOrUpdate(BasicMapper basicMapper, TableInfo tableInfo, Collection<T> list, SaveOrUpdateStrategy saveOrUpdateStrategy) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        int cnt = 0;
        for (T entity : list) {
            cnt += saveOrUpdate(basicMapper, tableInfo, entity, saveOrUpdateStrategy);
        }
        return cnt;
    }
}
