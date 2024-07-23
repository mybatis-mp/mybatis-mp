package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.ModelFieldInfo;
import cn.mybatis.mp.core.db.reflect.ModelInfo;
import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.db.reflect.TableIds;
import cn.mybatis.mp.core.incrementer.IdentifierGenerator;
import cn.mybatis.mp.core.incrementer.IdentifierGeneratorFactory;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.Insert;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.ModelInfoUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.TableField;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ModelInsertContext<T extends Model> extends SQLCmdInsertContext<BaseInsert> implements SetIdMethod {

    private final T model;

    private final ModelInfo modelInfo;

    public ModelInsertContext(T model) {
        this.model = model;
        this.modelInfo = Models.get(model.getClass());
        this.entityType = modelInfo.getEntityType();
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
        TenantUtil.setTenantId(model);
        Insert insert = new Insert();
        CmdFactory $ = insert.$();
        Table table = $.table(modelInfo.getTableInfo().getSchemaAndTableName());
        insert.insert(table);
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < modelInfo.getFieldSize(); i++) {
            ModelFieldInfo modelFieldInfo = modelInfo.getModelFieldInfos().get(i);
            boolean isNeedInsert = false;
            Object value = modelFieldInfo.getValue(model);
            if (modelFieldInfo.getTableFieldInfo().isTableId()) {
                if (!IdUtil.isIdExists(value)) {
                    TableId tableId = TableIds.get(modelInfo.getTableInfo().getType(), dbType);
                    if (tableId.value() == IdAutoType.GENERATOR) {
                        isNeedInsert = true;
                        IdentifierGenerator identifierGenerator = IdentifierGeneratorFactory.getIdentifierGenerator(tableId.generatorName());
                        Object id = identifierGenerator.nextId(modelInfo.getType());
                        if (modelInfo.getIdFieldInfo().getField().getType() == String.class) {
                            id = id instanceof String ? id : String.valueOf(id);
                        }
                        if (IdUtil.setId(model, modelFieldInfo, id)) {
                            value = id;
                            isNeedInsert = true;
                        }
                    }
                } else {
                    isNeedInsert = true;
                }
            } else if (Objects.nonNull(value)) {
                isNeedInsert = true;
            } else if (!StringPool.EMPTY.equals(modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation().defaultValue())) {
                isNeedInsert = true;

                //设置默认值
                value = MybatisMpConfig.getDefaultValue(modelFieldInfo.getField().getType(), modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation().defaultValue());
                //默认值回写
                ModelInfoUtil.setValue(modelFieldInfo, model, value);
            } else if (modelFieldInfo.getTableFieldInfo().isVersion()) {
                isNeedInsert = true;

                //乐观锁设置 默认值1
                value = Integer.valueOf(1);
                //乐观锁回写
                ModelInfoUtil.setValue(modelFieldInfo, model, value);
            }

            if (isNeedInsert) {
                insert.field($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()));
                TableField tableField = modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation();
                MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                values.add($.value(mybatisParameter));
            }
        }
        insert.values(values);

        return insert;
    }

    @Override
    public void setId(Object id) {
        IdUtil.setId(this.model, this.modelInfo.getIdFieldInfo(), id);
    }

}
