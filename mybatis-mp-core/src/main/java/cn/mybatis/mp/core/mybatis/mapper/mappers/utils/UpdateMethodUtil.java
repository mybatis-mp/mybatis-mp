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

    public static <E> int update(BasicMapper basicMapper, TableInfo tableInfo, E entity, boolean allFieldForce, Getter<E>[] forceUpdateFields) {
        return update(basicMapper, tableInfo, entity, allFieldForce, LambdaUtil.getFieldNames(forceUpdateFields));
    }

    public static <E> int update(BasicMapper basicMapper, TableInfo tableInfo, E entity, boolean allFieldForce, Set<String> forceUpdateFields) {
        return basicMapper.$update(new EntityUpdateContext(tableInfo, entity, allFieldForce, forceUpdateFields));
    }

    public static <E> int update(BasicMapper basicMapper, TableInfo tableInfo, E entity, Where where, boolean allFieldForce, Set<String> forceUpdateFields) {
        return basicMapper.$update(new EntityUpdateWithWhereContext<>(tableInfo, entity, where, allFieldForce, forceUpdateFields));
    }

    public static <E> int update(BasicMapper basicMapper, TableInfo tableInfo, Collection<E> list, boolean allFieldForce, Getter<E>[] forceUpdateFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> forceFields = LambdaUtil.getFieldNames(forceUpdateFields);
        int cnt = 0;
        for (E entity : list) {
            cnt += update(basicMapper, tableInfo, entity, allFieldForce, forceFields);
        }
        return cnt;
    }

    public static <E> int update(BasicMapper basicMapper, TableInfo tableInfo, E entity, boolean allFieldForce, Consumer<Where> consumer) {
        return update(basicMapper, tableInfo, entity, WhereUtil.create(tableInfo, consumer), allFieldForce, null);
    }

    public static <E> int update(BasicMapper basicMapper, TableInfo tableInfo, E entity, Consumer<Where> consumer, Getter<E>[] forceUpdateFields) {
        return update(basicMapper, tableInfo, entity, WhereUtil.create(tableInfo, consumer), false, LambdaUtil.getFieldNames(forceUpdateFields));
    }

}
