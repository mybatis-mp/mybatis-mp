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

package db.sql.api.impl.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.cmd.basic.ICondition;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.dbFun.Case;
import db.sql.api.impl.cmd.dbFun.If;

import java.io.Serializable;

public interface Condition<Field extends Cmd, Value> extends ICondition, Cmd {
    char[] getOperator();

    Field getField();

    Value getValue();

    default If if_(Cmd value, Cmd value2) {
        return new If(this, value, value2);
    }

    default If if_(Cmd value, Serializable value2) {
        return new If(this, value, Methods.cmd(value2));
    }

    default If if_(Serializable value, Serializable value2) {
        return new If(this, value, value2);
    }

    default Case caseThen(Serializable value) {
        return this.caseThen(Methods.cmd(value));
    }

    default Case caseThen(Cmd value) {
        return new Case().when(this, value);
    }
}
