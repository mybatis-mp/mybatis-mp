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

package db.sql.api.impl.cmd.condition;

import db.sql.api.Cmd;
import db.sql.api.cmd.LikeMode;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.tookit.SqlConst;

public class NotLike extends Like {

    public NotLike(Cmd key, Cmd value) {
        this(LikeMode.DEFAULT, key, value);
    }

    public NotLike(Cmd key, Object value) {
        this(key, Methods.cmd(value));
    }

    public NotLike(LikeMode mode, Cmd key, Cmd value) {
        super(SqlConst.NOT_LIKE, mode, key, value);
    }

    public NotLike(LikeMode mode, Cmd key, Object value) {
        super(SqlConst.NOT_LIKE, mode, key, Methods.cmd(value));
    }
}
