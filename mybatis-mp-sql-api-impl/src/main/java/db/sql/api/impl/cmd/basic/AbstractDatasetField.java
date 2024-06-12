package db.sql.api.impl.cmd.basic;


import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.impl.cmd.struct.insert.InsertFields;
import db.sql.api.impl.cmd.struct.query.Select;
import db.sql.api.impl.tookit.SqlConst;
import db.sql.api.tookit.CmdUtils;

public abstract class AbstractDatasetField<T extends AbstractDatasetField<T>> extends AbstractField<T> implements IDatasetField<T> {

    private final IDataset table;

    private final String name;

    public AbstractDatasetField(IDataset table, String name) {
        this.table = table;
        this.name = name;
    }

    @Override
    public IDataset getTable() {
        return table;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getName(DbType dbType) {
        return dbType.wrap(this.name);
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        // insert 直接名字
        if (parent instanceof InsertFields) {
            sqlBuilder.append(getName(context.getDbType()));
            return sqlBuilder;
        }

        if (this.table.getAlias() != null) {
            sqlBuilder.append(SqlConst.BLANK).append(this.table.getAlias()).append(SqlConst.DOT);
        } else {
            sqlBuilder.append(SqlConst.BLANK);
        }

        sqlBuilder.append(getName(context.getDbType()));

        //拼接 select 的别名
        if (parent instanceof Select) {
            //有别名
            if (this.getAlias() != null && !SqlConst.S_EMPTY.equals(this.getAlias())) {
                if (this.getAlias().equals(this.getName())) {
                    //别名和列名一致 不需要AS ；直接退出
                    return sqlBuilder;
                }
                //拼接上 AS
                sqlBuilder.append(SqlConst.AS(context.getDbType()));
                return sqlBuilder.append(this.getAlias());
            }


            if (!(getTable() instanceof Table)) {
                //非 TableField 不继续处理
                return sqlBuilder;
            }

            String prefix = ((Table) getTable()).getPrefix();
            if (prefix == null || SqlConst.S_EMPTY.equals(prefix)) {
                //无表前缀 不继续处理
                return sqlBuilder;
            }

            //有表前缀 需要AS
            sqlBuilder.append(SqlConst.AS(context.getDbType()));
            //拼接上表前缀
            sqlBuilder.append(prefix);
            //拼接上列名
            sqlBuilder.append(getName());
            return sqlBuilder;
        }

        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.table);
    }
}
