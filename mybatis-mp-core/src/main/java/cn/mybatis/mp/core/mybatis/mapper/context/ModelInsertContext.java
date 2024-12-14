/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.db.reflect.ModelFieldInfo;
import cn.mybatis.mp.core.db.reflect.ModelInfo;
import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.db.reflect.TableIds;
import cn.mybatis.mp.core.incrementer.IdentifierGenerator;
import cn.mybatis.mp.core.incrementer.IdentifierGeneratorFactory;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.Insert;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.DefaultValueUtil;
import cn.mybatis.mp.core.util.ModelInfoUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.core.util.TypeConvertUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.TableField;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.NULL;
import db.sql.api.impl.cmd.basic.Table;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ModelInsertContext<T extends Model> extends SQLCmdInsertContext<BaseInsert> implements SetIdMethod {

    private final T model;

    private final ModelInfo modelInfo;

    private final boolean allFieldForce;

    private final Set<String> forceFields;

    private final boolean idHasValue;

    public ModelInsertContext(T model, boolean allFieldForce, Set<String> forceFields) {
        this.model = model;
        this.allFieldForce = allFieldForce;
        this.modelInfo = Models.get(model.getClass());
        this.entityType = modelInfo.getEntityType();
        this.forceFields = forceFields;
        this.idHasValue = IdUtil.isIdExists(model, modelInfo.getIdFieldInfo());
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
        MybatisCmdFactory $ = insert.$();
        $.cacheTableInfo(modelInfo.getTableInfo());

        Table table = $.table(modelInfo.getTableInfo().getSchemaAndTableName());
        insert.insert(table);
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < modelInfo.getFieldSize(); i++) {
            ModelFieldInfo modelFieldInfo = modelInfo.getModelFieldInfos().get(i);
            boolean isNeedInsert = false;
            Object value = modelFieldInfo.getValue(model);
            if (modelFieldInfo.getTableFieldInfo().isTableId()) {
                if (!IdUtil.isIdValueExists(value)) {
                    TableId tableId = TableIds.get(modelInfo.getTableInfo().getType(), dbType);
                    if (tableId.value() == IdAutoType.GENERATOR) {
                        isNeedInsert = true;
                        IdentifierGenerator identifierGenerator = IdentifierGeneratorFactory.getIdentifierGenerator(tableId.generatorName());
                        Object id = identifierGenerator.nextId(modelInfo.getType());
                        if (IdUtil.setId(model, modelFieldInfo, id)) {
                            value = id;
                        }
                    }
                } else {
                    isNeedInsert = true;
                }
            } else if (Objects.nonNull(value)) {
                isNeedInsert = true;
            } else if (modelFieldInfo.getTableFieldInfo().isLogicDelete()) {
                //逻辑删除字段
                //设置删除初始值
                value = modelFieldInfo.getTableFieldInfo().getLogicDeleteInitValue();
                if (value != null) {
                    isNeedInsert = true;
                    //逻辑删除初始值回写
                    ModelInfoUtil.setValue(modelFieldInfo, model, value);
                } else if (!StringPool.EMPTY.equals(modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation().defaultValue())) {
                    //读取回填 @TableField里的默认值
                    value = DefaultValueUtil.getAndSetDefaultValue(model, modelFieldInfo);
                    isNeedInsert = Objects.nonNull(value);
                }
            } else if (!StringPool.EMPTY.equals(modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation().defaultValue())) {
                //读取回填 默认值
                value = DefaultValueUtil.getAndSetDefaultValue(model, modelFieldInfo);
                isNeedInsert = Objects.nonNull(value);
            } else if (modelFieldInfo.getTableFieldInfo().isVersion()) {
                isNeedInsert = true;

                //乐观锁设置 默认值1
                value = TypeConvertUtil.convert(Integer.valueOf(1), modelFieldInfo.getField().getType());
                //乐观锁回写
                ModelInfoUtil.setValue(modelFieldInfo, model, value);
            }

            // 看是否是强制字段
            if (!isNeedInsert && (allFieldForce || (Objects.nonNull(this.forceFields) && this.forceFields.contains(modelFieldInfo.getField().getName())))) {
                isNeedInsert = true;
            }

            if (isNeedInsert) {
                insert.fields($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()));
                if (Objects.isNull(value)) {
                    values.add(NULL.NULL);
                } else {
                    TableField tableField = modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation();
                    MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                    values.add(Methods.value(mybatisParameter));
                }
            }
        }
        insert.values(values);

        return insert;
    }

    @Override
    public void setId(Object id, int index) {
        IdUtil.setId(this.model, this.modelInfo.getSingleIdFieldInfo(true), id);
    }

    @Override
    public boolean idHasValue() {
        return idHasValue;
    }

    @Override
    public int getInsertSize() {
        return 1;
    }

    @Override
    public Object getInsertObject(int index) {
        return this.model;
    }

    @Override
    public TypeHandler<?> getIdTypeHandler(Configuration configuration) {
        if (Objects.nonNull(this.modelInfo.getIdFieldInfo())) {
            TypeHandler typeHandler = this.modelInfo.getIdFieldInfo().getTableFieldInfo().getTypeHandler();
            if (Objects.isNull(typeHandler)) {
                return configuration.getTypeHandlerRegistry().getTypeHandler(this.modelInfo.getIdFieldInfo().getTableFieldInfo().getFieldInfo().getTypeClass());
            }
        }
        return null;
    }

    @Override
    public String getIdColumnName() {
        return this.modelInfo.getTableInfo().getIdFieldInfo().getColumnName();
    }
}
