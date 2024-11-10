package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityBatchInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityInsertContext;
import cn.mybatis.mp.core.util.TableInfoUtil;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import db.sql.api.Getter;
import db.sql.api.impl.tookit.LambdaUtil;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class SaveMethodUtil {

    public static <T> int save(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce) {
        return basicMapper.$saveEntity(new EntityInsertContext(tableInfo, entity, allFieldForce, null));
    }

    public static <T> int save(BasicMapper basicMapper, TableInfo tableInfo, T entity, Getter<T>[] forceSaveFields) {
        return basicMapper.$saveEntity(new EntityInsertContext(tableInfo, entity, false, LambdaUtil.getFieldNames(forceSaveFields)));
    }

    public static <E> int save(BasicMapper basicMapper, TableInfo tableInfo, Collection<E> list, boolean allFieldForce) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }

        int cnt = 0;
        for (E entity : list) {
            cnt += save(basicMapper, tableInfo, entity, allFieldForce);
        }
        return cnt;
    }

    public static <T> int save(BasicMapper basicMapper, TableInfo tableInfo, Collection<T> list, Getter<T>[] forceSaveFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }

        int cnt = 0;
        for (T entity : list) {
            cnt += save(basicMapper, tableInfo, entity, forceSaveFields);
        }
        return cnt;
    }

    public static <E> int saveBatch(BasicMapper basicMapper, TableInfo tableInfo, Collection<E> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> saveFieldSet = new HashSet<>();
        DbType dbType = basicMapper.getCurrentDbType();
        for (TableFieldInfo tableFieldInfo : tableInfo.getTableFieldInfos()) {
            if (!tableFieldInfo.getTableFieldAnnotation().insert()) {
                continue;
            }
            if (tableFieldInfo.isTableId()) {
                TableId tableId = TableInfoUtil.getTableIdAnnotation(tableFieldInfo.getField(), dbType);
                Objects.requireNonNull(tableId.value());
                if (tableId.value() == IdAutoType.AUTO) {
                    Object id;
                    try {
                        id = tableFieldInfo.getReadFieldInvoker().invoke(list.stream().findFirst().get(), null);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                    if (Objects.isNull(id)) {
                        continue;
                    }
                }
            }
            saveFieldSet.add(tableFieldInfo.getField().getName());
        }
        return basicMapper.$save(new EntityBatchInsertContext(tableInfo, list, saveFieldSet));
    }

    public static <E> int saveBatch(BasicMapper basicMapper, TableInfo tableInfo, Collection<E> list, Getter<E>... saveFields) {
        if (Objects.isNull(saveFields) || saveFields.length < 1) {
            throw new RuntimeException("saveFields can't be null or empty");
        }
        return basicMapper.$save(new EntityBatchInsertContext(tableInfo, list, LambdaUtil.getFieldNames(saveFields)));
    }
}
