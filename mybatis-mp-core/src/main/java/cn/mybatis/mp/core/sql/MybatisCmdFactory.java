package cn.mybatis.mp.core.sql;

import cn.mybatis.mp.core.NotTableClassException;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.sql.executor.MpDatasetField;
import cn.mybatis.mp.core.sql.executor.MpTable;
import cn.mybatis.mp.core.sql.executor.MpTableField;
import cn.mybatis.mp.core.util.TableInfoUtil;
import db.sql.api.Getter;
import db.sql.api.cmd.basic.IDataset;
import db.sql.api.cmd.basic.IDatasetField;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.tookit.LambdaUtil;
import org.apache.ibatis.util.MapUtil;

import java.util.Objects;


/**
 * CMD 命令工厂
 * 增加了对实体类的映射
 */
public class MybatisCmdFactory extends CmdFactory {

    public MybatisCmdFactory() {
        super();
    }

    public MybatisCmdFactory(String tableAsPrefix) {
        super(tableAsPrefix);
    }

    @Override
    public Table table(Class entity, int storey) {
        return MapUtil.computeIfAbsent(this.tableCache, storey + entity.getName(), key -> {
            TableInfo tableInfo = Tables.get(entity);
            tableNums++;
            Table table = new MpTable(tableInfo);
            table.as(tableAs(storey, tableNums));
            return table;
        });
    }

    @Override
    public <T> TableField field(Getter<T> column, int storey) {
        LambdaUtil.LambdaFieldInfo fieldInfo = LambdaUtil.getFieldInfo(column);
        return this.field(fieldInfo.getType(), fieldInfo.getName(), storey);
    }

    @Override
    public <T> String columnName(Getter<T> column) {
        return TableInfoUtil.getColumnName(column);
    }

    @Override
    public TableField field(Class entity, String filedName, int storey) {
        TableInfo tableInfo;
        try {
            tableInfo = Tables.get(entity);
        } catch (NotTableClassException e) {
            throw new RuntimeException("class " + entity.getName() + " is not entity");
        }

        TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(filedName);
        if (Objects.isNull(tableFieldInfo)) {
            throw new RuntimeException("property " + filedName + " is not a column");
        }
        Table table = table(entity, storey);
        return new MpTableField((MpTable) table, tableFieldInfo);
    }

    @Override
    public <T> TableField[] fields(int storey, Getter<T>... columns) {
        if (columns.length < 2) {
            return new TableField[]{this.field(columns[0], 1)};
        }
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(columns[0]);
        TableInfo tableInfo = Tables.get(lambdaFieldInfo.getType());
        Table table = this.table(lambdaFieldInfo.getType(), storey);
        TableField[] tableFields = new TableField[columns.length];
        for (int i = 0; i < columns.length; i++) {
            tableFields[i] = table.$(tableInfo.getFieldInfo(LambdaUtil.getName(columns[i])).getColumnName());
        }
        return tableFields;
    }

    @Override
    public <T, DATASET extends IDataset<DATASET, DATASET_FIELD>, DATASET_FIELD extends IDatasetField<DATASET_FIELD>> DATASET_FIELD field(IDataset<DATASET, DATASET_FIELD> dataset, Getter<T> column) {
        LambdaUtil.LambdaFieldInfo fieldInfo = LambdaUtil.getFieldInfo(column);
        TableInfo tableInfo = Tables.get(fieldInfo.getType());
        TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(fieldInfo.getName());

        if (dataset instanceof MpTable) {
            return (DATASET_FIELD) new MpTableField((MpTable) dataset, tableFieldInfo);
        } else if (dataset instanceof Table) {
            return (DATASET_FIELD) new TableField((Table) dataset, tableFieldInfo.getColumnName());
        }
        return (DATASET_FIELD) new MpDatasetField(dataset, tableFieldInfo.getColumnName(), tableFieldInfo.getField().getType(), tableFieldInfo.getTypeHandler(), tableFieldInfo.getTableFieldAnnotation().jdbcType());
    }
}
