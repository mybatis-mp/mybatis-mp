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

package cn.mybatis.mp.core.mybatis;

import db.sql.api.DbType;

/**
 * 当参数有Where 参数时，才使用
 */
public final class DbTypeHolder {

    private static final ThreadLocal<DbType> dbTypeHolder = new ThreadLocal<>();

    public static void setDbType(DbType dbType) {
        dbTypeHolder.set(dbType);
    }

    public static DbType getDbType() {
        return dbTypeHolder.get();
    }

    public static void clearDbType() {
        dbTypeHolder.remove();
    }
}
