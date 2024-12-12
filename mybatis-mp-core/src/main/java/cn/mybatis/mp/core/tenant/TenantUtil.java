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

package cn.mybatis.mp.core.tenant;

import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.core.sql.executor.MpDatasetField;
import cn.mybatis.mp.core.sql.executor.MpTable;
import cn.mybatis.mp.db.Model;
import db.sql.api.impl.cmd.struct.ConditionChain;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Objects;

public final class TenantUtil {

    public static Serializable getTenantId() {

        return TenantContext.getTenantId();
    }

    /**
     * 设置实体类的租户ID
     *
     * @param model 实体类model
     */
    public static void setTenantId(Model model) {
        ModelInfo modelInfo = Models.get(model.getClass());
        if (Objects.isNull(modelInfo.getTenantIdFieldInfo())) {
            return;
        }

        Serializable tenantId = getTenantId();
        if (Objects.isNull(tenantId)) {
            return;
        }

        try {
            modelInfo.getTenantIdFieldInfo().getWriteFieldInvoker().invoke(model, new Object[]{
                    tenantId
            });
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置实体类的租户ID
     *
     * @param entity
     */
    public static Serializable setTenantId(Object entity) {
        TableInfo tableInfo = Tables.get(entity.getClass());
        return setTenantId(tableInfo, entity);
    }

    /**
     * 设置实体类的租户ID
     *
     * @param entity
     */
    public static Serializable setTenantId(TableInfo tableInfo, Object entity) {

        if (Objects.isNull(tableInfo.getTenantIdFieldInfo())) {
            return null;
        }

        Serializable tenantId = getTenantId();
        if (Objects.isNull(tenantId)) {
            return null;
        }

        try {
            tableInfo.getTenantIdFieldInfo().getWriteFieldInvoker().invoke(entity, new Object[]{
                    tenantId
            });
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return tenantId;
    }

    /**
     * 添加租户条件
     *
     * @param table          MpTable
     * @param conditionChain ConditionChain
     */
    public static void addTenantCondition(MpTable table, ConditionChain conditionChain) {
        Serializable tenantId = TenantUtil.getTenantId();
        if (Objects.isNull(tenantId)) {
            return;
        }
        TableInfo tableInfo = table.getTableInfo();
        if (Objects.isNull(tableInfo.getTenantIdFieldInfo())) {
            return;
        }
        addTenantCondition(table, conditionChain, tableInfo.getTenantIdFieldInfo(), tenantId);
    }

    /**
     * 添加租户条件
     *
     * @param table          MpTable
     * @param where Where
     * @param tenantTableField 租户ID字段
     */
    public static Serializable addTenantCondition(MpTable table, Where where, TableFieldInfo tenantTableField) {
        Serializable tenantId = TenantUtil.getTenantId();
        addTenantCondition(table, where.extConditionChain(), tenantTableField, tenantId);
        return tenantId;
    }

    /**
     * 添加租户条件
     *
     * @param table            MpTable
     * @param where            Where
     * @param tenantTableField 租户ID 字段tableFieldInfo
     * @param tenantId         租户ID
     */
    public static void addTenantCondition(MpTable table, Where where, TableFieldInfo tenantTableField, Object tenantId) {
        addTenantCondition(table, where.extConditionChain(), tenantTableField, tenantId);
    }

    /**
     * 添加租户条件
     *
     * @param table            MpTable
     * @param conditionChain   ConditionChain
     * @param tenantTableField 租户ID 字段tableFieldInfo
     * @param tenantId         租户ID
     */
    public static void addTenantCondition(MpTable table, ConditionChain conditionChain, TableFieldInfo tenantTableField, Object tenantId) {
        if (Objects.isNull(tenantId)) {
            return;
        }
        conditionChain.eq(new MpDatasetField(table, tenantTableField.getColumnName(),
                tenantTableField.getFieldInfo(), tenantTableField.getTypeHandler(),
                tenantTableField.getTableFieldAnnotation().jdbcType()), tenantId);
    }
}
