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

package db.sql.api.cmd.executor.method.groupByMethod;


import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;

import java.util.function.Function;

public interface IGroupByMultiGetterMethod<SELF extends IGroupByMultiGetterMethod, TABLE extends ITable<TABLE, TABLE_FIELD>, TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>> {

    default <T> SELF groupBy(Getter<T>... columns) {
        return this.groupBy(1, columns);
    }

    <T> SELF groupBy(int storey, Getter<T>... columns);

    default <T> SELF groupBy(boolean when, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(1, columns);
    }

    default <T> SELF groupBy(boolean when, int storey, Getter<T>... columns) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(storey, columns);
    }

    //---

    SELF groupBy(GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f);

    default SELF groupBy(boolean when, GetterField[] getterFields, Function<TABLE_FIELD[], Cmd> f) {
        if (!when) {
            return (SELF) this;
        }
        return this.groupBy(getterFields, f);
    }
}
