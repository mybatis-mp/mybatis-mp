package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.annotations.TableField;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.NULL;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Objects;
import java.util.Set;

public class EntityUpdateCmdCreateUtil {

    private static Update warp(Update update, TableInfo tableInfo, Object t, Set<String> forceUpdateFields) {

        MybatisCmdFactory $ = update.$();

        Table table = $.table(t.getClass());
        update.update(table);

        boolean hasIdCondition = false;

        for (int i = 0; i < tableInfo.getFieldSize(); i++) {
            TableFieldInfo tableFieldInfo = tableInfo.getTableFieldInfos().get(i);

            boolean isForceUpdate = forceUpdateFields.contains(tableFieldInfo.getField().getName());
            if (!isForceUpdate && !tableFieldInfo.getTableFieldAnnotation().update()) {
                continue;
            }

            Object value = tableFieldInfo.getValue(t);

            if (tableFieldInfo.isTableId()) {
                if (Objects.nonNull(value)) {
                    update.$where().extConditionChain().eq($.field(table, tableFieldInfo.getColumnName()), Methods.cmd(value));
                    hasIdCondition = true;
                }
                continue;
            } else if (tableFieldInfo.isTenantId()) {
                //添加租户条件
                Object tenantId = TenantUtil.addTenantCondition(update.$where(), update.$(), t.getClass(), tableFieldInfo, 1);
                if (Objects.nonNull(tenantId)) {
                    //租户回写
                    TableInfoUtil.setValue(tableFieldInfo, t, tenantId);
                }
                continue;
            } else if (tableFieldInfo.isVersion()) {
                if (Objects.isNull(value)) {
                    //乐观锁字段无值 不增加乐观锁条件
                    continue;
                }
                Integer version = (Integer) value + 1;
                //乐观锁设置
                update.set($.field(table, tableFieldInfo.getColumnName()), Methods.value(version));

                //乐观锁条件
                update.$where().extConditionChain().eq($.field(table, tableFieldInfo.getColumnName()), Methods.cmd(value));

                //乐观锁回写
                TableInfoUtil.setValue(tableFieldInfo, t, version);
                continue;
            }

            if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().updateDefaultValue())) {
                //设置默认值
                value = MybatisMpConfig.getDefaultValue(tableFieldInfo.getField().getType(), tableFieldInfo.getTableFieldAnnotation().updateDefaultValue());
                //默认值回写
                TableInfoUtil.setValue(tableFieldInfo, t, value);
            }

            if (isForceUpdate) {
                if (Objects.isNull(value)) {
                    update.set($.field(table, tableFieldInfo.getColumnName()), NULL.NULL);
                    continue;
                }
            }

            if (Objects.nonNull(value)) {
                TableField tableField = tableFieldInfo.getTableFieldAnnotation();
                MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                update.set($.field(table, tableFieldInfo.getColumnName()), Methods.value(mybatisParameter));
            }
        }

        if (!hasIdCondition) {
            if (update.getWhere() == null || !update.getWhere().hasContent()) {
                throw new RuntimeException("update has no where condition content ");
            }
        }
        return update;
    }


    public static Update create(Object entity, Set<String> forceUpdateFields) {
        TableInfo tableInfo = Tables.get(entity.getClass());
        if (!tableInfo.isHasMultiId()) {
            Object id = TableInfoUtil.getEntityIdValue(tableInfo, entity, true);
            if (Objects.isNull(id)) {
                throw new RuntimeException(" can't found id value");
            }
        }
        return warp(new Update(), tableInfo, entity, forceUpdateFields);
    }

    public static Update create(Object entity, Where where, Set<String> forceUpdateFields) {
        if (Objects.isNull(where) || !where.hasContent()) {
            throw new RuntimeException("update has no where condition content ");
        }
        TableInfo tableInfo = Tables.get(entity.getClass());
        return warp(new Update(where), tableInfo, entity, forceUpdateFields);
    }
}
