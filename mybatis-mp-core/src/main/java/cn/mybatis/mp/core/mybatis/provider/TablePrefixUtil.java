package cn.mybatis.mp.core.mybatis.provider;


import cn.mybatis.mp.core.db.reflect.ResultInfos;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.db.annotations.ResultEntity;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.executor.AbstractQuery;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public final class TablePrefixUtil {

    private TablePrefixUtil() {
    }

    public static void prefixMapping(AbstractQuery query, Class returnType) {
        if (Objects.isNull(returnType)) {
            return;
        }
        if (!returnType.isAnnotationPresent(ResultEntity.class)) {
            return;
        }

        Map<Class, Map<Integer, String>> entityPrefixMap = ResultInfos.get(returnType).getTablePrefixes();
        if (Objects.nonNull(entityPrefixMap)) {
            Iterator<Map.Entry<Class, Map<Integer, String>>> iterator = entityPrefixMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Class, Map<Integer, String>> entry = iterator.next();
                Class<?> entityType = entry.getKey();
                entry.getValue().forEach((storey, prefix) -> {
                    if (storey == -1) {
                        for (int i = 0; i < 5; i++) {
                            Table table = query.$().cacheTable(entityType, storey);
                            if (Objects.nonNull(table) && Objects.isNull(table.getPrefix())) {
                                table.setPrefix(prefix);
                                break;
                            }
                        }
                    } else {
                        Table table = query.$().cacheTable(entityType, storey);
                        if (Objects.nonNull(table) && Objects.isNull(table.getPrefix())) {
                            table.setPrefix(prefix);
                        }
                    }
                });
            }
        }
    }
}
