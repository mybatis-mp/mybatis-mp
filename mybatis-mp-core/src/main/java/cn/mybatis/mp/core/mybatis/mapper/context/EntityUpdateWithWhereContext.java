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

package cn.mybatis.mp.core.mybatis.mapper.context;


import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.sql.executor.Update;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Set;

public class EntityUpdateWithWhereContext<T> extends SQLCmdUpdateContext {

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where) {
        this(tableInfo, t, where, null);
    }

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where, Set<String> forceFields) {
        this(tableInfo, t, where, false, forceFields);
    }

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where, boolean allFieldForce) {
        this(tableInfo, t, where, allFieldForce, null);
    }

    public EntityUpdateWithWhereContext(TableInfo tableInfo, T t, Where where, boolean allFieldForce, Set<String> forceFields) {
        super(createCmd(tableInfo, t, where, allFieldForce, forceFields));
    }


    private static Update createCmd(TableInfo tableInfo, Object t, Where where, boolean allFieldForce, Set<String> forceFields) {
        return EntityUpdateCmdCreateUtil.create(tableInfo, t, where, allFieldForce, forceFields);
    }
}
