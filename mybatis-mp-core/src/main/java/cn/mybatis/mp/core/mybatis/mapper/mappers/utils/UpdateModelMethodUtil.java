package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelUpdateContext;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelUpdateWithWhereContext;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import db.sql.api.impl.tookit.LambdaUtil;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

public final class UpdateModelMethodUtil {

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Getter<MODEL>[] forceUpdateFields) {
        return update(basicMapper, model, allFieldForce, LambdaUtil.getFieldNames(forceUpdateFields));
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Set<String> forceUpdateFields) {
        return basicMapper.$update(new ModelUpdateContext(model, allFieldForce, forceUpdateFields));
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, Collection<MODEL> list, boolean allFieldForce, Getter<MODEL>[] forceUpdateFields) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        Set<String> forceFields = LambdaUtil.getFieldNames(forceUpdateFields);
        int cnt = 0;
        for (MODEL model : list) {
            cnt += update(basicMapper, model, allFieldForce, forceFields);
        }
        return cnt;
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, boolean allFieldForce, Consumer<Where> consumer) {
        return update(basicMapper, model, WhereUtil.create(consumer), allFieldForce, null);
    }

    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, Consumer<Where> consumer, Getter<MODEL>[] forceUpdateFields) {
        return update(basicMapper, model, WhereUtil.create(consumer), false, LambdaUtil.getFieldNames(forceUpdateFields));
    }


    public static <MODEL extends Model> int update(BasicMapper basicMapper, MODEL model, Where where, boolean allFieldForce, Set<String> forceUpdateFields) {
        return basicMapper.$update(new ModelUpdateWithWhereContext<>(model, where, allFieldForce, forceUpdateFields));
    }
}
