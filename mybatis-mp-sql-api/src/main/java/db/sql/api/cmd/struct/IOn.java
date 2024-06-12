package db.sql.api.cmd.struct;


import db.sql.api.Cmd;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.executor.method.IConditionMethod;
import db.sql.api.cmd.struct.conditionChain.IConditionChain;

public interface IOn<SELF extends IOn<SELF, JOIN, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        JOIN extends IJoin<JOIN, SELF, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        COLUMN extends Cmd,
        V,
        CONDITION_CHAIN extends IConditionChain<CONDITION_CHAIN, TABLE_FIELD, COLUMN, V>>
        extends IConditionMethod<SELF, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        Cmd {


    JOIN getJoin();
}
