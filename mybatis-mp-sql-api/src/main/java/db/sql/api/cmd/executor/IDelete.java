package db.sql.api.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.executor.method.IDeleteMethod;
import db.sql.api.cmd.executor.method.IFromMethod;
import db.sql.api.cmd.executor.method.IJoinMethod;
import db.sql.api.cmd.executor.method.IWhereMethod;
import db.sql.api.cmd.struct.IFrom;
import db.sql.api.cmd.struct.IJoin;
import db.sql.api.cmd.struct.IOn;
import db.sql.api.cmd.struct.IWhere;
import db.sql.api.cmd.struct.conditionChain.IConditionChain;
import db.sql.api.cmd.struct.delete.IDeleteTable;

public interface IDelete<SELF extends IDelete,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd,
        V,
        CONDITION_CHAIN extends IConditionChain<CONDITION_CHAIN, TABLE_FIELD, COLUMN, V>,
        DELETE_TABLE extends IDeleteTable,
        FROM extends IFrom,
        JOIN extends IJoin<JOIN, ON, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        ON extends IOn<ON, JOIN, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        WHERE extends IWhere<WHERE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>>

        extends IDeleteMethod<SELF>,
        IFromMethod<SELF, TABLE, TABLE_FIELD>,
        IJoinMethod<SELF, ON>,
        IWhereMethod<SELF, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        IExecutor<SELF, TABLE, TABLE_FIELD> {

    DELETE_TABLE $delete(IDataset... tables);

    FROM $from(IDataset table);

    JOIN $join(JoinMode mode, IDataset mainTable, IDataset secondTable);

    WHERE $where();

    @Override
    default <DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> SELF delete(IDataset<DATASET, DATASET_FIELD>... tables) {
        $delete(tables);
        return (SELF) this;
    }

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
