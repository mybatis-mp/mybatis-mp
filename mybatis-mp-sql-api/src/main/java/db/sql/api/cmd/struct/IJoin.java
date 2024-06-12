package db.sql.api.cmd.struct;

import db.sql.api.Cmd;
import db.sql.api.cmd.JoinMode;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.struct.conditionChain.IConditionChain;

public interface IJoin<SELF extends IJoin<SELF, ON, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>
        , ON extends IOn<ON, SELF, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>
        , TABLE extends ITable<TABLE, TABLE_FIELD>
        , TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>
        , COLUMN extends Cmd
        , V
        , CONDITION_CHAIN extends IConditionChain<CONDITION_CHAIN, TABLE_FIELD, COLUMN, V>
        > extends Cmd {

    IDataset getMainTable();

    IDataset getSecondTable();

    JoinMode getMode();

    ON getOn();

}
