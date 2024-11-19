package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.core.util.TypeConvertUtil;
import cn.mybatis.mp.db.annotations.TableField;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.NULL;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Objects;
import java.util.Set;

public class EntityUpdateCmdCreateUtil {

    private static Update warp(Update update, TableInfo tableInfo, Object t, Set<String> forceFields, boolean allFieldForce) {

        MybatisCmdFactory $ = update.$();

        Table table = $.table(t.getClass());
        update.update(table);

        boolean hasIdCondition = false;

        for (int i = 0; i < tableInfo.getFieldSize(); i++) {
            TableFieldInfo tableFieldInfo = tableInfo.getTableFieldInfos().get(i);

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
                //乐观锁+1
                Object version = TypeConvertUtil.convert(Long.valueOf(1) + 1, tableFieldInfo.getField().getType());
                //乐观锁设置
                update.set($.field(table, tableFieldInfo.getColumnName()), Methods.cmd(version));
                //乐观锁条件
                update.$where().extConditionChain().eq($.field(table, tableFieldInfo.getColumnName()), Methods.cmd(value));
                //乐观锁回写
                TableInfoUtil.setValue(tableFieldInfo, t, version);
                continue;
            }

            if (!StringPool.EMPTY.equals(tableFieldInfo.getTableFieldAnnotation().updateDefaultValue())) {
                //设置默认值
                value = MybatisMpConfig.getDefaultValue(tableFieldInfo.getFieldInfo().getTypeClass(), tableFieldInfo.getTableFieldAnnotation().updateDefaultValue());
                //默认值回写
                TableInfoUtil.setValue(tableFieldInfo, t, value);
            }

            boolean isForceUpdate = Objects.nonNull(forceFields) && forceFields.contains(tableFieldInfo.getField().getName());
            if (!isForceUpdate && !tableFieldInfo.getTableFieldAnnotation().update()) {
                continue;
            }

            if (isForceUpdate || allFieldForce) {
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

    public static Update create(TableInfo tableInfo, Object entity, boolean allFieldForce, Set<String> forceFields) {
        return warp(new Update(), tableInfo, entity, forceFields, allFieldForce);
    }

    public static Update create(TableInfo tableInfo, Object entity, Where where, boolean allFieldForce, Set<String> forceFields) {
        if (Objects.isNull(where) || !where.hasContent()) {
            throw new RuntimeException("update has no where condition content ");
        }
        return warp(new Update(where), tableInfo, entity, forceFields, allFieldForce);
    }
}
