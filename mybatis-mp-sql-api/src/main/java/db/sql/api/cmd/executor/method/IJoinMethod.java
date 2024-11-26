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

package db.sql.api.cmd.executor.method;

import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;

import java.util.function.Consumer;

public interface IJoinMethod<SELF extends IJoinMethod, ON> {

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF join(DATASET mainTable, DATASET secondTable, Consumer<ON> consumer) {
        return this.join(JoinMode.INNER, mainTable, secondTable, consumer);
    }

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>, DATASET2 extends IDataset<DATASET2, DATASET_FIELD2>, DATASET_FIELD2 extends IDatasetField<DATASET_FIELD2>> SELF join(JoinMode mode, DATASET mainTable, DATASET2 secondTable, Consumer<ON> consumer);

    default SELF join(JoinMode mode, Class mainTable, Class secondTable) {
        return join(mode, mainTable, secondTable, null);
    }

    default SELF join(Class mainTable, Class secondTable) {
        return join(JoinMode.INNER, mainTable, secondTable);
    }

    default SELF join(Class mainTable, Class secondTable, Consumer<ON> consumer) {
        return join(JoinMode.INNER, mainTable, secondTable, consumer);
    }

    default SELF join(JoinMode mode, Class mainTable, Class secondTable, Consumer<ON> consumer) {
        return join(mode, mainTable, 1, secondTable, 1, consumer);
    }

    default SELF join(Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey) {
        return join(JoinMode.INNER, mainTable, mainTableStorey, secondTable, secondTableStorey);
    }

    default SELF join(JoinMode mode, Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey) {
        return join(mode, mainTable, mainTableStorey, secondTable, secondTableStorey, null);
    }

    default SELF join(Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, Consumer<ON> consumer) {
        return join(JoinMode.INNER, mainTable, mainTableStorey, secondTable, secondTableStorey, consumer);
    }

    SELF join(JoinMode mode, Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, Consumer<ON> consumer);


    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF join(Class mainTable, DATASET secondTable, Consumer<ON> consumer) {
        return join(JoinMode.INNER, mainTable, secondTable, consumer);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF join(JoinMode mode, Class mainTable, DATASET secondTable, Consumer<ON> consumer) {
        return join(mode, mainTable, 1, secondTable, consumer);
    }

    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF join(Class mainTable, int mainTableStorey, DATASET secondTable, Consumer<ON> consumer) {
        return join(JoinMode.INNER, mainTable, mainTableStorey, secondTable, consumer);
    }

    <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF join(JoinMode mode, Class mainTable, int mainTableStorey, DATASET secondTable, Consumer<ON> consumer);

    /**
     * 实体类拦截
     *
     * @param mainTable
     * @param mainTableStorey
     * @param secondTable
     * @param consumer
     * @return
     */
    default Consumer<ON> joinEntityIntercept(Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey, Consumer<ON> consumer) {
        return consumer;
    }
}
