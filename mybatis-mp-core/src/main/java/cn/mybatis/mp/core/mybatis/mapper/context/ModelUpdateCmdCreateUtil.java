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
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.core.util.DefaultValueUtil;
import cn.mybatis.mp.core.util.ModelInfoUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.core.util.TypeConvertUtil;
import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.TableField;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.NULL;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Objects;
import java.util.Set;

public class ModelUpdateCmdCreateUtil {


    private static Update warp(Update update, ModelInfo modelInfo, Model t, Set<String> forceFields, boolean allFieldForce) {
        MybatisCmdFactory $ = update.$();
        Table table = $.table(modelInfo.getEntityType());
        update.update(table);

        boolean hasIdCondition = false;
        for (int i = 0; i < modelInfo.getFieldSize(); i++) {
            ModelFieldInfo modelFieldInfo = modelInfo.getModelFieldInfos().get(i);
            Object value = modelFieldInfo.getValue(t);
            if (modelFieldInfo.getTableFieldInfo().isTableId()) {
                if (Objects.nonNull(value)) {
                    update.$where().extConditionChain().eq($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), Methods.cmd(value));
                    hasIdCondition = true;
                }
                continue;
            } else if (modelFieldInfo.getTableFieldInfo().isTenantId()) {
                //租户ID不修改
                continue;
            } else if (modelFieldInfo.getTableFieldInfo().isVersion()) {
                if (Objects.isNull(value)) {
                    //乐观锁字段无值 不增加乐观锁条件
                    continue;
                }
                //乐观锁+1
                Object version = TypeConvertUtil.convert(Long.valueOf(value.toString()) + 1, modelFieldInfo.getField().getType());
                //乐观锁设置
                update.set($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), Methods.cmd(version));
                //乐观锁条件
                update.$where().extConditionChain().eq($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), Methods.cmd(value));
                //乐观锁回写
                ModelInfoUtil.setValue(modelFieldInfo, t, version);
                continue;
            }

            if (!StringPool.EMPTY.equals(modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation().updateDefaultValue())) {
                //读取回填 修改默认值
                value = DefaultValueUtil.getAndSetUpdateDefaultValue(t, modelFieldInfo);
            }

            if (allFieldForce || (Objects.nonNull(forceFields) && forceFields.contains(modelFieldInfo.getField().getName()))) {
                if (Objects.isNull(value)) {
                    update.set($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), NULL.NULL);
                    continue;
                }
            }

            if (Objects.nonNull(value)) {
                TableField tableField = modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation();
                MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                update.set($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), Methods.value(mybatisParameter));
            }
        }

        if (!hasIdCondition) {
            if (update.getWhere() == null || !update.getWhere().hasContent()) {
                throw new RuntimeException("update has no where condition content ");
            }
        }
        return update;
    }


    public static Update create(Model model, Set<String> forceFields, boolean allFieldForce) {
        Where where = WhereUtil.create();
        ModelInfo modelInfo = Models.get(model.getClass());
        return warp(new Update(where), modelInfo, model, forceFields, allFieldForce);
    }

    public static Update create(Model model, Where where, Set<String> forceFields, boolean allFieldForce) {
        if (Objects.isNull(where) || !where.hasContent()) {
            throw new RuntimeException("update has no where condition content ");
        }
        ModelInfo modelInfo = Models.get(model.getClass());
        return warp(new Update(where), modelInfo, model, forceFields, allFieldForce);
    }
}
