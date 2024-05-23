package cn.mybatis.mp.core.util;

import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class WhereUtil {
    public static Where create() {
        return cn.mybatis.mp.core.sql.executor.Where.create();
    }

    /**
     * 创建和消费
     *
     * @param consumer
     * @return
     */
    public static Where where(Consumer<Where> consumer) {
        Where where = create();
        consumer.accept(where);
        return where;
    }

    /**
     * 拼接id条件
     *
     * @param where
     * @param tableInfo
     * @param id
     */
    public static void appendIdWhere(Where where, TableInfo tableInfo, Serializable id) {
        TableInfoUtil.checkId(tableInfo);
        CmdFactory $ = where.getConditionFactory().getCmdFactory();
        where.eq($.field(tableInfo.getType(), tableInfo.getIdFieldInfo().getField().getName(), 1), id);
    }

    /**
     * 使用 IN 拼接多个id条件
     *
     * @param where     where
     * @param tableInfo 表信息
     * @param ids
     */
    public static void appendIdsWhere(Where where, TableInfo tableInfo, Serializable[] ids) {
        appendIdsWhere(where, tableInfo, Arrays.asList(ids));
    }

    /**
     * 使用 IN 拼接多个id条件
     *
     * @param where     where
     * @param tableInfo 表信息
     * @param ids
     */
    public static void appendIdsWhere(Where where, TableInfo tableInfo, List<Serializable> ids) {
        TableInfoUtil.checkId(tableInfo);
        CmdFactory $ = where.getConditionFactory().getCmdFactory();
        where.in($.field(tableInfo.getType(), tableInfo.getIdFieldInfo().getField().getName(), 1), ids);
    }

    /**
     * 添加version条件
     *
     * @param where
     * @param tableInfo
     * @param entity
     * @param <T>
     */
    public static <T> void appendVersionWhere(Where where, TableInfo tableInfo, T entity) {
        TableFieldInfo versionFieldInfo = tableInfo.getVersionFieldInfo();
        if (Objects.isNull(versionFieldInfo)) {
            return;
        }

        Object version = versionFieldInfo.getValue(entity);
        if (Objects.isNull(version)) {
            return;
        }

        CmdFactory $ = where.getConditionFactory().getCmdFactory();
        where.eq($.field(entity.getClass(), versionFieldInfo.getField().getName(), 1), version);
    }
}
