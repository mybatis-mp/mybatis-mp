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

package db.sql.api.impl.cmd.executor;

import db.sql.api.DbType;
import db.sql.api.cmd.executor.Runnable;
import db.sql.api.impl.tookit.Objects;

import java.util.HashMap;
import java.util.Map;

public class DbSelector implements Selector {

    private final Map<DbType, Runnable> consumers = new HashMap<>();

    private Runnable otherwise;

    public DbSelector when(DbType dbType, Runnable runnable) {
        consumers.put(dbType, runnable);
        return this;
    }

    public DbSelector when(DbType[] dbTypes, Runnable runnable) {
        for (DbType dbType : dbTypes) {
            consumers.put(dbType, runnable);
        }
        return this;
    }

    public DbSelector otherwise(Runnable runnable) {
        if (Objects.nonNull(this.otherwise)) {
            throw new RuntimeException("The method of 'otherwise' has already called");
        }
        this.otherwise = runnable;
        return this;
    }

    public DbSelector otherwise() {
        return this.otherwise(() -> {
        });
    }

    public void dbExecute(DbType dbType) {
        Runnable runnable = consumers.get(dbType);
        if (Objects.nonNull(runnable)) {
            runnable.run();
            return;
        }
        if (Objects.nonNull(this.otherwise)) {
            this.otherwise.run();
            return;
        }
        throw new RuntimeException("Not adapted to DbType " + dbType);
    }
}
