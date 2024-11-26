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

package db.sql.api.cmd.executor.method.condition.compare;

import db.sql.api.Getter;

import java.io.Serializable;

public interface INotBetweenGetterCompare<RV> {

    default <T> RV notBetween(Getter<T> column, Serializable value, Serializable value2) {
        return notBetween(true, column, 1, value, value2);
    }

    default <T> RV notBetween(boolean when, Getter<T> column, Serializable value, Serializable value2) {
        return this.notBetween(when, column, 1, value, value2);
    }

    default <T> RV notBetween(Getter<T> column, int storey, Serializable value, Serializable value2) {
        return notBetween(true, column, storey, value, value2);
    }

    <T> RV notBetween(boolean when, Getter<T> column, int storey, Serializable value, Serializable value2);
}
