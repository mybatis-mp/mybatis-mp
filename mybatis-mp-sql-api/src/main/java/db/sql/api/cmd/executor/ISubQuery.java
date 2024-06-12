package db.sql.api.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.cmd.ICmdFactory;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.cmd.basic.ITable;
import db.sql.api.cmd.basic.ITableField;
import db.sql.api.cmd.struct.*;
import db.sql.api.cmd.struct.conditionChain.IConditionChain;
import db.sql.api.cmd.struct.query.*;

/**
 * 子查询
 *
 * @param <SELF>
 * @param <TABLE>
 * @param <TABLE_FIELD>
 * @param <DATASET_FIELD>
 * @param <COLUMN>
 * @param <V>
 * @param <CMD_FACTORY>
 * @param <CONDITION_CHAIN>
 * @param <SELECT>
 * @param <FROM>
 * @param <JOIN>
 * @param <ON>
 * @param <JOINS>
 * @param <WHERE>
 * @param <GROUPBY>
 * @param <HAVING>
 * @param <ORDERBY>
 * @param <LIMIT>
 * @param <FORUPDATE>
 * @param <UNION>
 */
public interface ISubQuery<SELF extends ISubQuery<SELF, TABLE, TABLE_FIELD, DATASET_FIELD, COLUMN, V, CMD_FACTORY, CONDITION_CHAIN, WITH, SELECT, FROM, JOIN, ON, JOINS, WHERE, GROUPBY, HAVING, ORDERBY, LIMIT, FORUPDATE, UNION>,
        TABLE extends ITable<TABLE, TABLE_FIELD>,
        TABLE_FIELD extends ITableField<TABLE_FIELD, TABLE>,
        DATASET_FIELD extends IDatasetField<DATASET_FIELD>,
        COLUMN extends Cmd,
        V,
        CMD_FACTORY extends ICmdFactory<TABLE, TABLE_FIELD>,
        CONDITION_CHAIN extends IConditionChain<CONDITION_CHAIN, TABLE_FIELD, COLUMN, V>,

        WITH extends IWith<WITH>,
        SELECT extends ISelect<SELECT>,
        FROM extends IFrom,
        JOIN extends IJoin<JOIN, ON, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        ON extends IOn<ON, JOIN, TABLE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        JOINS extends Joins<JOIN>,
        WHERE extends IWhere<WHERE, TABLE_FIELD, COLUMN, V, CONDITION_CHAIN>,
        GROUPBY extends IGroupBy<GROUPBY, COLUMN>,
        HAVING extends IHaving<HAVING>,
        ORDERBY extends IOrderBy<ORDERBY>,
        LIMIT extends ILimit<LIMIT>,
        FORUPDATE extends IForUpdate<FORUPDATE>,
        UNION extends IUnion
        > extends IQuery<
        SELF,
        TABLE,
        TABLE_FIELD,
        COLUMN,
        V,
        CMD_FACTORY,
        CONDITION_CHAIN,
        WITH,
        SELECT,
        FROM,
        JOIN,
        ON,
        JOINS,
        WHERE,
        GROUPBY,
        HAVING,
        ORDERBY,
        LIMIT,
        FORUPDATE,
        UNION
        >, IDataset<SELF, DATASET_FIELD> {


}
