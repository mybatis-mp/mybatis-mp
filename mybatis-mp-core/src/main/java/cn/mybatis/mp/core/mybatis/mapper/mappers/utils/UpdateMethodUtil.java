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
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityUpdateContext;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.UpdateStrategy;

import java.util.Collection;
import java.util.Objects;

public final class UpdateMethodUtil {

    public static <T> UpdateStrategy<T> createUpdateStrategy() {
        return new UpdateStrategy<>();
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity) {
        return update(basicMapper, tableInfo, entity, createUpdateStrategy());
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, UpdateStrategy<T> updateStrategy) {
        return basicMapper.$update(new EntityUpdateContext(tableInfo, entity, updateStrategy));
    }

    public static <T> int updateList(BasicMapper basicMapper, Collection<T> list, UpdateStrategy<T> updateStrategy) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        TableInfo tableInfo = Tables.get(list.stream().findFirst().get().getClass());
        return updateList(basicMapper, tableInfo, list, updateStrategy);
    }

    public static <T> int updateList(BasicMapper basicMapper, TableInfo tableInfo, Collection<T> list, UpdateStrategy<T> updateStrategy) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }

        int cnt = 0;
        for (T entity : list) {
            cnt += update(basicMapper, tableInfo, entity, updateStrategy);
        }
        return cnt;
    }
}
