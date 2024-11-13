package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.sql.util.QueryUtil;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public final class ExistsMethodUtil {

    public static boolean exists(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer) {
        return exists(basicMapper, tableInfo, WhereUtil.create(tableInfo, consumer));
    }

    public static boolean exists(BasicMapper basicMapper, TableInfo tableInfo, Where where) {
        return basicMapper.exists(QueryUtil.buildNoOptimizationQuery(tableInfo, where, q -> q.select1()));
    }
}
