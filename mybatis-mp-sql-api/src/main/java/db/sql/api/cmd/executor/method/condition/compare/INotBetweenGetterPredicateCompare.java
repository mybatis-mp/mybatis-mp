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
import java.util.function.Predicate;

public interface INotBetweenGetterPredicateCompare<RV> {

    default <T, V2> RV notBetween(Getter<T> column, V2 value, V2 value2, Predicate<V2> predicate) {
        return notBetween(column, 1, value, value2, predicate);
    }

    default <T, V2> RV notBetween(Getter<T> column, int storey, V2 value, V2 value2, Predicate<V2> predicate) {
        return this.notBetween(predicate.test(value) && predicate.test(value2), column, storey, (Serializable) value, (Serializable) value2);
    }

    <T> RV notBetween(boolean when, Getter<T> column, int storey, Serializable value, Serializable value2);
}
