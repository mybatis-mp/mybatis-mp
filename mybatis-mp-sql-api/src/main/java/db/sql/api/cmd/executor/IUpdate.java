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

package db.sql.api.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.UpdateStrategy;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.executor.method.IFromMethod;
import db.sql.api.cmd.executor.method.IJoinMethod;
import db.sql.api.cmd.executor.method.IUpdateMethod;
import db.sql.api.cmd.executor.method.IWhereMethod;
import db.sql.api.cmd.struct.IFrom;
import db.sql.api.cmd.struct.IJoin;
import db.sql.api.cmd.struct.IOn;
import db.sql.api.cmd.struct.IWhere;
import db.sql.api.cmd.struct.conditionChain.IConditionChain;
import db.sql.api.cmd.struct.update.IUpdateTable;

import java.util.function.Consumer;

public interface IUpdate<SELF extends IUpdate,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd,
        V,
        CONDITION_CHAIN extends IConditionChain<CONDITION_CHAIN, TABLE_FIELD, COLUMN, V>,
        UPDATE_TABLE extends IUpdateTable<TABLE>,
        FROM extends IFrom,
        JOIN extends IJoin<JOIN, ON, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        ON extends IOn<ON, JOIN, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        WHERE extends IWhere<WHERE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>
        >

        extends IUpdateMethod<SELF, TABLE, TABLE_FIELD, V>,
        IFromMethod<SELF, TABLE, TABLE_FIELD>,
        IJoinMethod<SELF, ON>,
        IWhereMethod<SELF, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        IExecutor<SELF, TABLE, TABLE_FIELD> {


    UPDATE_TABLE $update(TABLE... tables);

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> FROM $from(IDataset<DATASET, DATASET_FIELD> table);

    JOIN $join(JoinMode mode, IDataset mainTable, IDataset secondTable);

    WHERE $where();

    @Override
    default SELF update(TABLE... tables) {
        $update(tables);
        return (SELF) this;
    }

    SELF update(Class entity, Consumer<TABLE> consumer);

    default <T> SELF set(Getter<T> field, V value) {
        return this.set(field, value, false);
    }

    default <T> SELF set(Getter<T> field, V value, boolean nullToNull) {
        return this.set(field, value, nullToNull ? UpdateStrategy.NULL_TO_NULL : UpdateStrategy.THROW_EXCEPTION);
    }

    <T> SELF set(Getter<T> field, V value, UpdateStrategy updateStrategy);

    default SELF set(TABLE_FIELD field, V value, boolean nullToNull) {
        return this.set(field, value, nullToNull ? UpdateStrategy.NULL_TO_NULL : UpdateStrategy.THROW_EXCEPTION);
    }

    SELF set(TABLE_FIELD filed, V value, UpdateStrategy updateStrategy);

    @Override
    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF from(IDataset<DATASET, DATASET_FIELD>... tables) {
        for (IDataset<DATASET, DATASET_FIELD> table : tables) {
            $from(table);
        }
        return (SELF) this;
    }

    @Override
    default CONDITION_CHAIN conditionChain() {
        return $where().conditionChain();
    }
}
