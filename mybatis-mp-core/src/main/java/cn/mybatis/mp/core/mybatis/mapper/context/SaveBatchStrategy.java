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


import cn.mybatis.mp.core.sql.executor.BaseInsert;
import db.sql.api.Getter;
import db.sql.api.tookit.LambdaUtil;

import java.util.Set;
import java.util.function.Consumer;

@lombok.Getter
public class SaveBatchStrategy<T> {

    private Set<String> forceFields;

    private Consumer<BaseInsert<?>> beforeListener;

    public SaveBatchStrategy<T> create() {
        return new SaveBatchStrategy();
    }

    public SaveBatchStrategy<T> forceFields(Getter<T>... forceFields) {
        this.forceFields = LambdaUtil.getFieldNames(forceFields);
        return this;
    }

    public SaveBatchStrategy<T> onBefore(Consumer<BaseInsert<?>> listener) {
        this.beforeListener = listener;
        return this;
    }
}
