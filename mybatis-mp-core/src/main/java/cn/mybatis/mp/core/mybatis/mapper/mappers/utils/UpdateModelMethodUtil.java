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

import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelUpdateContext;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelUpdateWithWhereContext;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.tookit.LambdaUtil;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class UpdateModelMethodUtil {

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Set<String> forceFields) {
        return basicMapper.$update(new ModelUpdateContext<>(model, allFieldForce, forceFields));
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Getter<MODEL>[] forceFields) {
        return update(basicMapper, model, allFieldForce, LambdaUtil.getFieldNames(forceFields));
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, Collection<MODEL> list, boolean allFieldForce, Getter<MODEL>[] forceFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> forceFieldNames = LambdaUtil.getFieldNames(forceFields);
        int cnt = 0;
        for (MODEL model : list) {
            cnt += update(basicMapper, model, allFieldForce, forceFieldNames);
        }
        return cnt;
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Getter<MODEL>[] forceFields, Consumer<Where> consumer) {
        return update(basicMapper, model, allFieldForce, LambdaUtil.getFieldNames(forceFields), WhereUtil.create(consumer));
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Getter<MODEL>[] forceFields, Where where) {
        return update(basicMapper, model, allFieldForce, LambdaUtil.getFieldNames(forceFields), where);
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Set<String> forceFields, Where where) {
        return basicMapper.$update(new ModelUpdateWithWhereContext<>(model, where, allFieldForce, forceFields));
    }
}
