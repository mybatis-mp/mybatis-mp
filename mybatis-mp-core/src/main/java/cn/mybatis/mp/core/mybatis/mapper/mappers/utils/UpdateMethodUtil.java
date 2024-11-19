package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityUpdateContext;
import cn.mybatis.mp.core.mybatis.mapper.context.EntityUpdateWithWhereContext;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.LambdaUtil;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class UpdateMethodUtil {

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Set<String> forceFields) {
        return basicMapper.$update(new EntityUpdateContext(tableInfo, entity, allFieldForce, forceFields));
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Getter<T>[] forceFields) {
        return update(basicMapper, tableInfo, entity, allFieldForce, LambdaUtil.getFieldNames(forceFields));
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, Where where, boolean allFieldForce, Set<String> forceFields) {
        return basicMapper.$update(new EntityUpdateWithWhereContext<>(tableInfo, entity, where, allFieldForce, forceFields));
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, Collection<T> list, boolean allFieldForce, Getter<T>[] forceFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> forceFieldNames = LambdaUtil.getFieldNames(forceFields);
        int cnt = 0;
        for (T entity : list) {
            cnt += update(basicMapper, tableInfo, entity, allFieldForce, forceFieldNames);
        }
        return cnt;
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, boolean allFieldForce, Consumer<Where> consumer) {
        return update(basicMapper, tableInfo, entity, WhereUtil.create(tableInfo, consumer), allFieldForce, null);
    }

    public static <T> int update(BasicMapper basicMapper, TableInfo tableInfo, T entity, Consumer<Where> consumer, Getter<T>[] forceFields) {
        return update(basicMapper, tableInfo, entity, WhereUtil.create(tableInfo, consumer), false, LambdaUtil.getFieldNames(forceFields));
    }

}
