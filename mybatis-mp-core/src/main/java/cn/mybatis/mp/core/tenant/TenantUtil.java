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
import cn.mybatis.mp.db.Model;
import db.sql.api.impl.cmd.CmdFactory;
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
     * @param where      where
     * @param cmdFactory 命令工厂
     * @param entity     实体类
     * @param storey     实体类表的存储层级
     */
    public static void addTenantCondition(Where where, CmdFactory cmdFactory, Class entity, int storey) {
        Serializable tenantId = TenantUtil.getTenantId();
        if (Objects.isNull(tenantId)) {
            return;
        }
        TableInfo tableInfo = Tables.get(entity);
        if (Objects.isNull(tableInfo.getTenantIdFieldInfo())) {
            return;
        }
        where.extConditionChain().eq(cmdFactory.field(entity, tableInfo.getTenantIdFieldInfo().getField().getName(), storey), tenantId);
    }

    /**
     * 添加租户条件
     *
     * @param where      where
     * @param cmdFactory 命令工厂
     * @param tableInfo  tableInfo
     * @param storey     实体类表的存储层级
     */
    public static void addTenantCondition(Where where, CmdFactory cmdFactory, TableInfo tableInfo, int storey) {
        Serializable tenantId = TenantUtil.getTenantId();
        if (Objects.isNull(tenantId)) {
            return;
        }

        if (Objects.isNull(tableInfo.getTenantIdFieldInfo())) {
            return;
        }
        where.extConditionChain().eq(cmdFactory.field(tableInfo.getType(), tableInfo.getTenantIdFieldInfo().getField().getName(), storey), tenantId);
    }

    /**
     * 添加租户条件
     *
     * @param where      where
     * @param cmdFactory 命令工厂
     * @param entity     实体类
     * @param storey     实体类表的存储层级
     */
    public static Serializable addTenantCondition(Where where, CmdFactory cmdFactory, Class entity, TableFieldInfo tenantTableField, int storey) {
        Serializable tenantId = TenantUtil.getTenantId();
        if (Objects.isNull(tenantId)) {
            return null;
        }
        where.extConditionChain().eq(cmdFactory.field(entity, tenantTableField.getField().getName(), storey), tenantId);
        return tenantId;
    }
}
