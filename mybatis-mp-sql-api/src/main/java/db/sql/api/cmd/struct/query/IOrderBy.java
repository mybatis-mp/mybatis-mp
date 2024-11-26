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

package db.sql.api.cmd.struct.query;

import db.sql.api.Cmd;
import db.sql.api.cmd.basic.IOrderByDirection;

import java.util.List;

public interface IOrderBy<SELF extends IOrderBy> extends Cmd {

    SELF orderBy(IOrderByDirection orderByDirection, Cmd column);

    default SELF orderBy(IOrderByDirection orderByDirection, Cmd... columns) {
        for (Cmd column : columns) {
            this.orderBy(orderByDirection, column);
        }
        return (SELF) this;
    }


    default SELF orderBy(List<Cmd> columns, IOrderByDirection orderByDirection) {
        for (Cmd column : columns) {
            this.orderBy(orderByDirection, column);
        }
        return (SELF) this;
    }
}
