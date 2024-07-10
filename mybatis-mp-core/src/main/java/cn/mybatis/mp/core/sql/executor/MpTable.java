package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.util.TableInfoUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.tookit.LambdaUtil;

public class MpTable extends db.sql.api.impl.cmd.basic.Table {

    protected final TableInfo tableInfo;

    public MpTable(TableInfo tableInfo) {
        super(tableInfo.getSchemaAndTableName());
        this.tableInfo = tableInfo;
    }

    @Override
    public <E> TableField $(Getter<E> column) {
        LambdaUtil.LambdaFieldInfo fieldInfo = LambdaUtil.getFieldInfo(column);
        if (fieldInfo.getType() == tableInfo.getType()) {

        }
        return super.$(TableInfoUtil.getColumnName(column));
    }

    public TableInfo getTableInfo() {
        return tableInfo;
    }
}
