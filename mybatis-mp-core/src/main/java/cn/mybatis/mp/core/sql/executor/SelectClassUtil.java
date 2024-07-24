package cn.mybatis.mp.core.sql.executor;


import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.Table;
import db.sql.api.Cmd;
import db.sql.api.impl.cmd.executor.AbstractQuery;

import java.util.ArrayList;
import java.util.List;

public final class SelectClassUtil {

    private static List<Cmd> buildSelect(AbstractQuery query, List<ResultFieldInfo> resultFieldInfos, List<Cmd> cmdList) {
        resultFieldInfos.stream().filter(item -> item instanceof ResultTableFieldInfo)
                .forEach(item -> {
                    ResultTableFieldInfo resultTableFieldInfo = (ResultTableFieldInfo) item;
                    Cmd tableField = query.$().field(resultTableFieldInfo.getTableInfo().getType(), resultTableFieldInfo.getTableFieldInfo().getField().getName(), resultTableFieldInfo.getStorey());
                    if (!cmdList.contains(tableField)) {
                        cmdList.add(tableField);
                    }
                });
        return cmdList;
    }

    private static List<Cmd> buildNestedSelect(AbstractQuery query, List<NestedResultInfo> nestedResultInfos, List<Cmd> cmdList) {
        nestedResultInfos.stream().forEach(item -> {
            buildSelect(query, item.getResultFieldInfos(), cmdList);
            buildNestedSelect(query, item.getNestedResultInfos(), cmdList);
        });
        return cmdList;
    }

    private static List<Cmd> buildSelect(AbstractQuery query, Class clazz, int storey, List<Cmd> cmdList) {
        if (clazz.isAnnotationPresent(ResultEntity.class)) {
            ResultInfo resultInfo = ResultInfos.get(clazz);
            buildSelect(query, resultInfo.getResultFieldInfos(), cmdList);
            buildNestedSelect(query, resultInfo.getNestedResultInfos(), cmdList);
        } else if (clazz.isAnnotationPresent(Table.class)) {
            TableInfo tableInfo = Tables.get(clazz);
            for (int i = 0; i < tableInfo.getFieldSize(); i++) {
                TableFieldInfo tableFieldInfo = tableInfo.getTableFieldInfos().get(i);
                if (tableFieldInfo.getTableFieldAnnotation().select()) {
                    cmdList.add(query.$().field(clazz, tableFieldInfo.getField().getName(), storey));
                }
            }
        }
        return cmdList;
    }


    public static void select(AbstractQuery query, Class clazz, int storey) {
        query.select(buildSelect(query, clazz, storey, new ArrayList<>()));
    }

    public static void select(AbstractQuery query, int storey, Class[] entities) {
        List<Cmd> list = new ArrayList<>();
        for (Class entity : entities) {
            buildSelect(query, entity, storey, list);
        }
        query.select(list);
    }
}
