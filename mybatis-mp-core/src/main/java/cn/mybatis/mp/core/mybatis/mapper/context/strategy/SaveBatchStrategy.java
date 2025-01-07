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

package cn.mybatis.mp.core.mybatis.mapper.context.strategy;


import db.sql.api.Getter;
import db.sql.api.cmd.basic.IConflictAction;
import db.sql.api.tookit.LambdaUtil;

import java.util.Set;
import java.util.function.Consumer;

@lombok.Getter
public class SaveBatchStrategy<T> {

    private Set<String> forceFields;

    private Getter<T>[] conflictKeys;

    private Consumer<IConflictAction<T>> conflictAction;

    public SaveBatchStrategy<T> forceFields(Getter<T>... forceFields) {
        this.forceFields = LambdaUtil.getFieldNames(forceFields);
        return this;
    }

    public SaveBatchStrategy<T> conflictKeys(Getter<T>... conflictKeys) {
        this.conflictKeys = conflictKeys;
        return this;
    }

    public void onConflict(Consumer<IConflictAction<T>> conflictAction) {
        this.conflictAction = conflictAction;
    }
}
