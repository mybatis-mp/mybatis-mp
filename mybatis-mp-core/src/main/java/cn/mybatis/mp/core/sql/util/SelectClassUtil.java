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

package cn.mybatis.mp.core.sql.util;


import cn.mybatis.mp.core.db.reflect.*;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.Table;
import db.sql.api.Cmd;
import db.sql.api.impl.cmd.executor.AbstractQuery;

import java.util.ArrayList;
import java.util.List;

public final class SelectClassUtil {

    private static void buildSelect(AbstractQuery query, List<ResultFieldInfo> resultFieldInfos, List<Cmd> cmdList) {
        resultFieldInfos.stream().filter(item -> item instanceof ResultTableFieldInfo)
                .forEach(item -> {
                    ResultTableFieldInfo resultTableFieldInfo = (ResultTableFieldInfo) item;
                    Cmd tableField = query.$().field(resultTableFieldInfo.getTableInfo().getType(), resultTableFieldInfo.getTableFieldInfo().getField().getName(), resultTableFieldInfo.getStorey());
                    if (!cmdList.contains(tableField)) {
                        cmdList.add(tableField);
                    }
                });
    }

    private static void buildNestedSelect(AbstractQuery query, List<NestedResultInfo> nestedResultInfos, List<Cmd> cmdList) {
        nestedResultInfos.forEach(item -> {
            buildSelect(query, item.getResultFieldInfos(), cmdList);
            buildNestedSelect(query, item.getNestedResultInfos(), cmdList);
        });
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

    public static boolean select(AbstractQuery query, Class clazz) {
        return select(query, clazz, 1);
    }

    public static boolean select(AbstractQuery query, Class clazz, int storey) {
        List<Cmd> list = new ArrayList<>();
        query.select(buildSelect(query, clazz, storey, list));
        return !list.isEmpty();
    }

    public static void select(AbstractQuery query, int storey, Class[] entities) {
        List<Cmd> list = new ArrayList<>();
        for (Class entity : entities) {
            buildSelect(query, entity, storey, list);
        }
        query.select(list);
    }
}
