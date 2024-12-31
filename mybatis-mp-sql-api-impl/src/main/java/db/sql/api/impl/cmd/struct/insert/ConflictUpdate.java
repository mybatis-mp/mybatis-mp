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

package db.sql.api.impl.cmd.struct.insert;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IConflictUpdate;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.ConflictUpdateTableField;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.struct.update.UpdateSets;
import db.sql.api.tookit.CmdUtils;

public class ConflictUpdate<T> implements IConflictUpdate<T> {

    private final CmdFactory cmdFactory;

    private UpdateSets updateSets = new UpdateSets();

    public ConflictUpdate(CmdFactory cmdFactory) {
        this.cmdFactory = cmdFactory;
    }

    @Override
    public IConflictUpdate<T> set(Getter<T> field, Object value) {
        updateSets.set(cmdFactory.field(field), Methods.cmd(value));
        return this;
    }

    @Override
    public IConflictUpdate<T> overwrite(Getter<T>... fields) {
        for (Getter<T> field : fields) {
            TableField tableField = cmdFactory.field(field);
            updateSets.set(tableField, new ConflictUpdateTableField(tableField));
        }
        return this;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        return this.updateSets.sql(module, this, context, sqlBuilder);
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.updateSets);
    }
}
