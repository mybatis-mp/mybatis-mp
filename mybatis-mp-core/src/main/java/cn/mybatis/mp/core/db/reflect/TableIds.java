package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class TableIds {

    private static final Map<String, TableId> CACHE = new ConcurrentHashMap<>();

    private TableIds() {

    }

    public static TableId get(Class entity, DbType dbType) {
        return CACHE.computeIfAbsent(entity.getName() + dbType, key -> {

            TableFieldInfo tableFieldInfo = Tables.get(entity).getSingleIdFieldInfo(false);
            if (Objects.isNull(tableFieldInfo)) {
                return null;
            }
            return TableInfoUtil.getTableIdAnnotation(tableFieldInfo.getField(), dbType);
        });
    }

}
