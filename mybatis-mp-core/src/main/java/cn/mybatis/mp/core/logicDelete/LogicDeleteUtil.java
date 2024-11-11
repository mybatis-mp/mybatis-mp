package cn.mybatis.mp.core.logicDelete;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import cn.mybatis.mp.core.sql.executor.Update;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.core.tenant.TenantUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.db.annotations.LogicDelete;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 逻辑删除工具类
 */
public final class LogicDeleteUtil {

    /**
     * 在指定逻辑开关下执行
     *
     * @param state    开关状态
     * @param supplier 返回函数
     * @param <T> 返回值
     * @return 函数执行后的返回值
     */
    public static <T> T execute(boolean state, Supplier<T> supplier) {
        try (LogicDeleteSwitch ignore = LogicDeleteSwitch.with(state)) {
            return supplier.get();
        }
    }

    /**
     * 是否需要逻辑删除
     *
     * @param tableInfo 实体类tableInfo
     * @return 是否需要逻辑删除
     */
    public static boolean isNeedLogicDelete(TableInfo tableInfo) {
        return MybatisMpConfig.isLogicDeleteSwitchOpen() && Objects.nonNull(tableInfo.getLogicDeleteFieldInfo());
    }


    /**
     * 获取删除后的值
     *
     * @param logicDeleteFieldInfo
     * @return
     */
    public static Object getLogicAfterValue(TableFieldInfo logicDeleteFieldInfo) {
        Object value;
        LogicDelete logicDelete = logicDeleteFieldInfo.getLogicDeleteAnnotation();
        Class type = logicDeleteFieldInfo.getFieldInfo().getTypeClass();
        value = MybatisMpConfig.getDefaultValue(type, logicDelete.afterValue());
        if (value == null) {
            throw new RuntimeException("Unable to obtain deleted value，please use MybatisMpConfig.setDefaultValue(\"" + logicDelete.afterValue() + "\") to resolve it");
        }
        return value;
    }

    /**
     * 获取逻辑删除时间
     *
     * @param tableInfo
     * @return
     */
    public static Object getLogicDeleteTimeValue(TableInfo tableInfo) {
        String deleteTimeFieldName = tableInfo.getLogicDeleteFieldInfo().getLogicDeleteAnnotation().deleteTimeField();
        TableFieldInfo deleteTimeField = tableInfo.getFieldInfo(deleteTimeFieldName);
        if (Objects.isNull(deleteTimeField)) {
            throw new RuntimeException("the attribute: " + deleteTimeFieldName + " in @LogicDelete is not found in class: " + tableInfo.getType().getName());
        }

        Class type = deleteTimeField.getFieldInfo().getTypeClass();
        if (type == LocalDateTime.class) {
            return LocalDateTime.now();
        } else if (type == Date.class) {
            return new Date();
        } else if (type == Long.class) {
            return System.currentTimeMillis();
        } else if (type == Integer.class) {
            return (int) (System.currentTimeMillis() / 1000);
        } else {
            throw new RuntimeException("Unsupported types");
        }
    }

    /**
     * 设置逻辑删除字段值  例如： set deleted=1 和 删除时间设置
     *
     * @param baseUpdate
     * @param tableInfo
     */
    public static void addLogicDeleteUpdateSets(BaseUpdate baseUpdate, TableInfo tableInfo) {
        Class entityType = tableInfo.getType();
        TableField logicDeleteTableField = baseUpdate.$().field(entityType, tableInfo.getLogicDeleteFieldInfo().getField().getName(), 1);
        baseUpdate.set(logicDeleteTableField, getLogicAfterValue(tableInfo.getLogicDeleteFieldInfo()));
        addLogicDeleteCondition(baseUpdate.$where(), baseUpdate.$(), entityType, 1);

        String deleteTimeFieldName = tableInfo.getLogicDeleteFieldInfo().getLogicDeleteAnnotation().deleteTimeField();
        if (!StringPool.EMPTY.equals(deleteTimeFieldName)) {
            TableField logicDeleteTimeTableField = baseUpdate.$().field(entityType, deleteTimeFieldName, 1);
            baseUpdate.set(logicDeleteTimeTableField, getLogicDeleteTimeValue(tableInfo));
        }
    }

    /**
     * 构建公共的UpdateChain
     *
     * @param tableInfo
     * @return
     */
    private static BaseUpdate<?> buildCommonUpdate(TableInfo tableInfo) {
        return new Update()
                .update(tableInfo.getType())
                .connect(self -> LogicDeleteUtil.addLogicDeleteUpdateSets(self, tableInfo));
    }

    /**
     * 根据ID 进行逻辑删除操作
     * 实际为update操作
     *
     * @param mapper
     * @param tableInfo
     * @param id
     * @return
     */
    public static int logicDelete(BasicMapper mapper, TableInfo tableInfo, Serializable id) {
        BaseUpdate<?> update = buildCommonUpdate(tableInfo);
        WhereUtil.appendIdWhere(update.$where(), tableInfo, id);
        return mapper.update(update);
    }

    /**
     * 根据多个ID 进行逻辑删除操作
     * 实际为update操作
     *
     * @param mapper
     * @param tableInfo
     * @param ids
     * @return
     */
    public static int logicDelete(BasicMapper mapper, TableInfo tableInfo, Serializable... ids) {
        BaseUpdate<?> update = buildCommonUpdate(tableInfo);
        WhereUtil.appendIdsWhere(update.$where(), tableInfo, ids);
        return mapper.update(update);
    }


    /**
     * 根据List<ID> 进行逻辑删除操作
     * 实际为update操作
     *
     * @param mapper
     * @param tableInfo
     * @param ids
     * @return
     */
    public static int logicDelete(BasicMapper mapper, TableInfo tableInfo, List<Serializable> ids) {
        BaseUpdate<?> update = buildCommonUpdate(tableInfo);
        WhereUtil.appendIdsWhere(update.getWhere(), tableInfo, ids);
        return mapper.update(update);
    }

    /**
     * 根据where 执行逻辑删除操作
     * 实际为update操作
     *
     * @param mapper
     * @param tableInfo
     * @param where
     * @return
     */
    public static int logicDelete(BasicMapper mapper, TableInfo tableInfo, Where where) {
        Update update = new Update(where);
        update.update(update.$().table(tableInfo.getType()))
                .connect(self -> {
                    LogicDeleteUtil.addLogicDeleteUpdateSets(self, tableInfo);
                    TenantUtil.addTenantCondition(where, update.$(), tableInfo, 1);
                });
        return mapper.update(update);
    }


    /**
     * 添加逻辑删除条件
     *
     * @param where      Where
     * @param cmdFactory 命令工厂
     * @param entity     实体类
     * @param storey     实体类表的存储层级
     */
    public static void addLogicDeleteCondition(Where where, CmdFactory cmdFactory, Class entity, int storey) {
        if (!MybatisMpConfig.isLogicDeleteSwitchOpen()) {
            return;
        }

        TableInfo tableInfo = Tables.get(entity);
        if (Objects.isNull(tableInfo.getLogicDeleteFieldInfo())) {
            return;
        }
        Object logicBeforeValue = tableInfo.getLogicDeleteFieldInfo().getLogicDeleteInitValue();
        TableField tableField = cmdFactory.field(entity, tableInfo.getLogicDeleteFieldInfo().getField().getName(), storey);
        if (Objects.isNull(logicBeforeValue)) {
            where.extConditionChain().isNull(tableField);
        } else {
            where.extConditionChain().eq(tableField, logicBeforeValue);
        }
    }
}
