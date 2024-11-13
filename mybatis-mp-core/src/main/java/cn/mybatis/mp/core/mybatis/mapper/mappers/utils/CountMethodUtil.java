package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.sql.util.QueryUtil;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public final class CountMethodUtil {

    public static int count(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer) {
        return count(basicMapper, tableInfo, WhereUtil.create(tableInfo, consumer));
    }

    public static int count(BasicMapper basicMapper, TableInfo tableInfo, Where where) {
        return basicMapper.count(QueryUtil.buildNoOptimizationQuery(tableInfo, where, q -> {
            q.selectCount1();
            QueryUtil.fillQueryDefault(q, tableInfo);
        }));
    }

    public static int countAll(BasicMapper basicMapper, TableInfo tableInfo) {
        return basicMapper.count(QueryUtil.buildNoOptimizationQuery(tableInfo, q -> {
            q.selectCount1();
            QueryUtil.fillQueryDefault(q, tableInfo);
        }));
    }
}
