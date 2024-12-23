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
import cn.mybatis.mp.core.mybatis.mapper.context.EntityUpdateContext;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityUpdateWithWhereContext;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.tookit.LambdaUtil;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class UpdateMethodUtil {

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Set<String> forceFields) {
        return basicMapper.$update(new EntityUpdateContext(tableInfo, entity, allFieldForce, forceFields));
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Getter<T>[] forceFields) {
        return update(basicMapper, tableInfo, entity, allFieldForce, LambdaUtil.getFieldNames(forceFields));
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, Where where, boolean allFieldForce, Set<String> forceFields) {
        return basicMapper.$update(new EntityUpdateWithWhereContext<>(tableInfo, entity, where, allFieldForce, forceFields));
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, Collection<T> list, boolean allFieldForce, Getter<T>[] forceFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> forceFieldNames = LambdaUtil.getFieldNames(forceFields);
        int cnt = 0;
        for (T entity : list) {
            cnt += update(basicMapper, tableInfo, entity, allFieldForce, forceFieldNames);
        }
        return cnt;
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Getter<T>[] forceFields, Where where) {
        return update(basicMapper, tableInfo, entity, where, allFieldForce, LambdaUtil.getFieldNames(forceFields));
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Getter<T>[] forceFields, Consumer<Where> consumer) {
        return update(basicMapper, tableInfo, entity, allFieldForce, forceFields, WhereUtil.create(consumer));
    }

}
