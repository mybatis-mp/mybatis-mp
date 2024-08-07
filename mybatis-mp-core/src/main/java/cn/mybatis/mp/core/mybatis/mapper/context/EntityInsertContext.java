package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableIds;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.incrementer.IdentifierGenerator;
import cn.mybatis.mp.core.incrementer.IdentifierGeneratorFactory;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.Insert;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.TableField;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EntityInsertContext<T> extends SQLCmdInsertContext<BaseInsert> implements SetIdMethod {

    private final T entity;

    private final TableInfo tableInfo;

    public EntityInsertContext(T entity) {
        this.entity = entity;
        this.entityType = entity.getClass();
        this.tableInfo = Tables.get(entity.getClass());

    }

    @Override
    public void init(DbType dbType) {
        super.init(dbType);
        if (Objects.isNull(this.execution)) {
            this.execution = createCmd(dbType);
        }
    }

    private Insert createCmd(DbType dbType) {
        //设置租户ID
        TenantUtil.setTenantId(this.tableInfo, this.entity);

        Insert insert = new Insert();
        MybatisCmdFactory $ = insert.$();
        Table table = $.table(tableInfo.getSchemaAndTableName());
        insert.insert(table);
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < tableInfo.getFieldSize(); i++) {
            TableFieldInfo tableFieldInfo = tableInfo.getTableFieldInfos().get(i);

            if (!tableFieldInfo.getTableFieldAnnotation().insert()) {
                continue;
            }

            boolean isNeedInsert = false;
            Object value = tableFieldInfo.getValue(entity);
            if (tableFieldInfo.isTableId()) {
                if (!IdUtil.isIdExists(value)) {
                    TableId tableId = TableIds.get(entity.getClass(), dbType);
                    if (tableId.value() == IdAutoType.GENERATOR) {
                        isNeedInsert = true;
                        IdentifierGenerator identifierGenerator = IdentifierGeneratorFactory.getIdentifierGenerator(tableId.generatorName());
                        Object id = identifierGenerator.nextId(tableInfo.getType());
                        if (IdUtil.setId(entity, tableFieldInfo, id)) {
                            value = id;
                            isNeedInsert = true;
                        }
                    }
                } else {
                    isNeedInsert = true;
                }
            } else if (Objects.nonNull(value)) {
                isNeedInsert = true;
            } else if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().defaultValue())) {
                isNeedInsert = true;

                //设置默认值
                value = MybatisMpConfig.getDefaultValue(tableFieldInfo.getField().getType(), tableFieldInfo.getTableFieldAnnotation().defaultValue());
                //默认值回写
                TableInfoUtil.setValue(tableFieldInfo, entity, value);
            } else if (tableFieldInfo.isVersion()) {
                isNeedInsert = true;

                //乐观锁设置 默认值1
                value = Integer.valueOf(1);
                //乐观锁回写
                TableInfoUtil.setValue(tableFieldInfo, entity, value);
            }

            if (isNeedInsert) {
                insert.field($.field(table, tableFieldInfo.getColumnName()));
                TableField tableField = tableFieldInfo.getTableFieldAnnotation();
                MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                values.add(Methods.value(mybatisParameter));
            }
        }
        insert.values(values);

        return insert;
    }

    @Override
    public void setId(Object id) {
        IdUtil.setId(this.entity, this.tableInfo.getIdFieldInfo(), id);
    }
}
