package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.ModelInfo;
import cn.mybatis.mp.core.db.reflect.Models;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.basic.Table;

import java.util.Collection;
import java.util.Objects;

public class SaveOrUpdateModelMethodUtil {

    public static <M extends Model> int saveOrUpdate(BasicMapper basicMapper, ModelInfo modelInfo, M model, boolean allFieldForce) {
        if (modelInfo.getIdFieldInfos().isEmpty()) {
            throw new RuntimeException(modelInfo.getType().getName() + " has no id");
        }
        Object id;
        try {
            id = modelInfo.getIdFieldInfos().get(0).getReadFieldInvoker().invoke(model, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        if (Objects.isNull(id)) {
            return SaveModelMethodUtil.save(basicMapper, model, allFieldForce);
        }

        Query<?> query = Query.create();
        query.$().cacheTableInfo(modelInfo.getTableInfo());
        Table table = query.$(modelInfo.getTableInfo().getType());
        query.select1().from(table);

        WhereUtil.appendIdWhereWithModel(query.$where(), modelInfo, model);
        boolean exists = basicMapper.exists(query);
        if (exists) {
            return UpdateModelMethodUtil.update(basicMapper, model, allFieldForce, (Getter<M>[]) null);
        } else {
            return SaveModelMethodUtil.save(basicMapper, model, allFieldForce);
        }
    }

    public static <M extends Model> int saveOrUpdate(BasicMapper basicMapper, Collection<M> list, boolean allFieldForce) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        M first = list.stream().findFirst().get();
        ModelInfo modelInfo = Models.get(first.getClass());
        int cnt = 0;
        for (M model : list) {
            cnt += saveOrUpdate(basicMapper, modelInfo, model, allFieldForce);
        }
        return cnt;
    }
}
