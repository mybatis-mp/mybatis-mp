package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.ModelInfoUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.TableField;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Objects;
import java.util.Set;

public class ModelUpdateCmdCreateUtil {


    private static Update warp(Update update, ModelInfo modelInfo, Model t, Set<String> forceUpdateFields) {

        MybatisCmdFactory $ = update.$();

        Table table = $.table(modelInfo.getEntityType());
        update.update(table);

        boolean hasIdCondition = false;
        for (int i = 0; i < modelInfo.getFieldSize(); i++) {
            ModelFieldInfo modelFieldInfo = modelInfo.getModelFieldInfos().get(i);
            Object value = modelFieldInfo.getValue(t);
            if (modelFieldInfo.getTableFieldInfo().isTableId()) {
                if (Objects.nonNull(value)) {
                    update.$where().extConditionChain().eq($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), $.value(value));
                    hasIdCondition = true;
                }
                continue;
            } else if (modelFieldInfo.getTableFieldInfo().isTenantId()) {
                //添加租户条件
                Object tenantId = TenantUtil.addTenantCondition(update.$where(), update.$(), modelInfo.getEntityType(), modelFieldInfo.getTableFieldInfo(), 1);
                if (Objects.nonNull(tenantId)) {
                    //租户回写
                    ModelInfoUtil.setValue(modelFieldInfo, t, tenantId);
                }
                continue;
            } else if (modelFieldInfo.getTableFieldInfo().isVersion()) {
                if (Objects.isNull(value)) {
                    //乐观锁字段无值 不增加乐观锁条件
                    continue;
                }
                Integer version = (Integer) value + 1;
                //乐观锁设置
                update.set($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), $.value(version));

                //乐观锁条件
                update.$where().extConditionChain().eq($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), $.value(value));
                continue;
            }

            if (!StringPool.EMPTY.equals(modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation().updateDefaultValue())) {
                //设置默认值
                value = MybatisMpConfig.getDefaultValue(modelFieldInfo.getField().getType(), modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation().updateDefaultValue());
                //默认值回写
                ModelInfoUtil.setValue(modelFieldInfo, t, value);
            }

            if (forceUpdateFields.contains(modelFieldInfo.getField().getName())) {
                if (Objects.isNull(value)) {
                    update.set($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), $.NULL());
                    continue;
                }
            }

            if (Objects.nonNull(value)) {
                TableField tableField = modelFieldInfo.getTableFieldInfo().getTableFieldAnnotation();
                MybatisParameter mybatisParameter = new MybatisParameter(value, tableField.typeHandler(), tableField.jdbcType());
                update.set($.field(table, modelFieldInfo.getTableFieldInfo().getColumnName()), $.value(mybatisParameter));
            }
        }

        if (!hasIdCondition) {
            if (update.getWhere() == null || !update.getWhere().hasContent()) {
                throw new RuntimeException("update has no where condition content ");
            }
        }
        return update;
    }


    public static Update create(Model model, Set<String> forceUpdateFields) {
        ModelInfo modelInfo = Models.get(model.getClass());
        TableInfo tableInfo = Tables.get(modelInfo.getEntityType());
        Object id = ModelInfoUtil.getEntityIdValue(modelInfo, tableInfo, model, true);
        if (Objects.isNull(id)) {
            throw new RuntimeException(" can't found id value");
        }
        return warp(new Update(), modelInfo, model, forceUpdateFields);
    }

    public static Update create(Model model, Where where, Set<String> forceUpdateFields) {
        if (Objects.isNull(where) || !where.hasContent()) {
            throw new RuntimeException("update has no where condition content ");
        }
        ModelInfo modelInfo = Models.get(model.getClass());
        return warp(new Update(where), modelInfo, model, forceUpdateFields);
    }
}
