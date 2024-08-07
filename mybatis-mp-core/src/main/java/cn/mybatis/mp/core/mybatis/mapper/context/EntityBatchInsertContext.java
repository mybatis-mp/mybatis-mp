package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableIds;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.incrementer.IdentifierGenerator;
import cn.mybatis.mp.core.incrementer.IdentifierGeneratorFactory;
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
import db.sql.api.impl.cmd.basic.NULL;
import db.sql.api.impl.cmd.basic.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityBatchInsertContext<T> extends SQLCmdInsertContext<BaseInsert> {

    private final List<T> list;

    private final Set<String> saveFieldSet;


    public EntityBatchInsertContext(List<T> list, Set<String> saveFieldSet) {
        this.list = list;
        this.saveFieldSet = saveFieldSet;
    }

    private static Insert createCmd(List<?> list, Set<String> saveFieldSet, DbType dbType) {
        Insert insert = new Insert();
        Class<?> entityType = list.get(0).getClass();
        TableInfo tableInfo = Tables.get(entityType);
        Table table = insert.$().table(tableInfo.getSchemaAndTableName());
        insert.insert(table);


        List<TableFieldInfo> saveFieldInfoSet = saveFieldSet.stream().map(tableInfo::getFieldInfo).collect(Collectors.toList());

        TableId tableId = null;

        //拼上主键
        if (Objects.nonNull(tableInfo.getIdFieldInfo())) {
            tableId = TableIds.get(entityType, dbType);
            if (tableId.value() == IdAutoType.GENERATOR) {
                if (!saveFieldInfoSet.contains(tableInfo.getIdFieldInfo())) {
                    saveFieldInfoSet.add(tableInfo.getIdFieldInfo());
                }
            }
        }

        //拼上租户ID
        if (Objects.nonNull(tableInfo.getTenantIdFieldInfo())) {
            if (!saveFieldInfoSet.contains(tableInfo.getTenantIdFieldInfo())) {
                saveFieldInfoSet.add(tableInfo.getTenantIdFieldInfo());
            }
        }

        //拼上乐观锁
        if (Objects.nonNull(tableInfo.getVersionFieldInfo())) {
            if (!saveFieldInfoSet.contains(tableInfo.getVersionFieldInfo())) {
                saveFieldInfoSet.add(tableInfo.getVersionFieldInfo());
            }
        }

        //拼上逻辑删除
        if (Objects.nonNull(tableInfo.getLogicDeleteFieldInfo())) {
            if (!saveFieldInfoSet.contains(tableInfo.getLogicDeleteFieldInfo())) {
                saveFieldInfoSet.add(tableInfo.getLogicDeleteFieldInfo());
            }
        }

        //设置insert 列
        for (TableFieldInfo tableFieldInfo : saveFieldInfoSet) {
            insert.field(insert.$().field(table, tableFieldInfo.getColumnName()));
        }

        int fieldSize = saveFieldInfoSet.size();

        for (Object t : list) {
            List<Object> values = new ArrayList<>();
            for (int i = 0; i < fieldSize; i++) {
                TableFieldInfo tableFieldInfo = saveFieldInfoSet.get(i);
                Object value = tableFieldInfo.getValue(t);
                boolean hasValue = (!tableFieldInfo.isTableId() && Objects.nonNull(value)) || (tableFieldInfo.isTableId() && IdUtil.isIdExists(value));
                if (!hasValue) {
                    if (tableFieldInfo.isTableId()) {
                        if (tableId.value() == IdAutoType.GENERATOR) {
                            IdentifierGenerator identifierGenerator = IdentifierGeneratorFactory.getIdentifierGenerator(tableId.generatorName());
                            Object id = identifierGenerator.nextId(tableInfo.getType());
                            if (IdUtil.setId(t, tableFieldInfo, id)) {
                                value = id;
                            }
                        } else {
                            throw new RuntimeException(tableFieldInfo.getField().getName() + " has no value");
                        }
                    } else if (tableFieldInfo.isTenantId()) {
                        value = TenantUtil.setTenantId(t);
                    } else if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().defaultValue())) {
                        //设置默认值
                        value = MybatisMpConfig.getDefaultValue(tableFieldInfo.getField().getType(), tableFieldInfo.getTableFieldAnnotation().defaultValue());

                        //默认值回写
                        TableInfoUtil.setValue(tableFieldInfo, t, value);
                    } else if (tableFieldInfo.isVersion()) {
                        //乐观锁设置 默认值1
                        value = Integer.valueOf(1);

                        //乐观锁回写
                        TableInfoUtil.setValue(tableFieldInfo, t, value);
                    }
                }

                TableField tableField = tableFieldInfo.getTableFieldAnnotation();
                if (Objects.isNull(value)) {
                    values.add(NULL.NULL);
                } else {
                    MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                    values.add(Methods.value(mybatisParameter));
                }
            }
            insert.values(values);
        }
        return insert;
    }


    @Override
    public void init(DbType dbType) {
        super.init(dbType);
        if (Objects.isNull(this.execution)) {
            this.execution = createCmd(this.list, this.saveFieldSet, dbType);
        }
    }
}
