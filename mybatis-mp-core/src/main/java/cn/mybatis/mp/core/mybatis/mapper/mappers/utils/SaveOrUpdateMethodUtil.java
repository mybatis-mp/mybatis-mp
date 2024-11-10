package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.basic.Table;

import java.util.Collection;
import java.util.Objects;

public class SaveOrUpdateMethodUtil {

    public static <T> int saveOrUpdate(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce) {
        Class<?> entityType = entity.getClass();
        if (tableInfo.getIdFieldInfos().isEmpty()) {
            throw new RuntimeException(entityType.getName() + " has no id");
        }
        Object id;
        try {
            id = tableInfo.getIdFieldInfos().get(0).getReadFieldInvoker().invoke(entity, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (Objects.isNull(id)) {
            return SaveMethodUtil.save(basicMapper, tableInfo, entity, allFieldForce);
        }

        Query<T> query = Query.create();
        query.$().cacheTableInfo(tableInfo);
        Table table = query.$(entityType);
        query.select1().from(table);

        WhereUtil.appendIdWhereWithEntity(query.$where(), tableInfo, entity);
        boolean exists = basicMapper.exists(query);
        if (exists) {
            return UpdateMethodUtil.update(basicMapper, tableInfo, entity, allFieldForce, (Getter<T>[]) null);
        } else {
            return SaveMethodUtil.save(basicMapper, tableInfo, entity, allFieldForce);
        }
    }

    public static <T> int saveOrUpdate(BasicMapper basicMapper, TableInfo tableInfo, Collection<T> list, boolean allFieldForce) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        int cnt = 0;
        for (T entity : list) {
            cnt += saveOrUpdate(basicMapper, tableInfo, entity, allFieldForce);
        }
        return cnt;
    }
}
