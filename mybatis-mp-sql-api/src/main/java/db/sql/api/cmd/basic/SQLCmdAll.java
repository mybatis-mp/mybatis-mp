package db.sql.api.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.SqlBuilderContext;
import db.sql.api.tookit.CmdUtils;

import java.util.Objects;

public class SQLCmdAll implements Cmd {

    public final static SQLCmdAll INSTANCE = new SQLCmdAll();

    private IDataset<?,?> dataset;

    public SQLCmdAll(){

    }

    public SQLCmdAll(IDataset<?,?> dataset){
        this.dataset=dataset;
    }

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        if(Objects.isNull(dataset)){
            sqlBuilder.append(" * ");
        }else{
            String name;
            if(dataset instanceof ITable){
                ITable table= (ITable)dataset;
                 name=Objects.isNull(table.getAlias())?table.getName():table.getAlias();
            }else{
                name=dataset.getAlias();
            }
            sqlBuilder.append(name).append(".* ");
        }
        return sqlBuilder;
    }

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd,dataset);
    }
}
