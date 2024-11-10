package cn.mybatis.mp.core.mybatis.mapper.mappers.utils;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.logicDelete.LogicDeleteUtil;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.sql.executor.Delete;
import cn.mybatis.mp.core.sql.util.WhereUtil;
import db.sql.api.cmd.basic.SQL1;
import db.sql.api.impl.cmd.struct.Where;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;

public final class DeleteMethodUtil {

    public static int deleteById(BasicMapper basicMapper, TableInfo tableInfo, Serializable id) {
        return delete(basicMapper, tableInfo, WhereUtil.create(tableInfo, w -> WhereUtil.appendIdWhere(w, tableInfo, id)));
    }

    public static int deleteByIds(BasicMapper basicMapper, TableInfo tableInfo, Serializable[] ids) {
        return delete(basicMapper, tableInfo, WhereUtil.create(tableInfo, w -> WhereUtil.appendIdsWhere(w, tableInfo, ids)));
    }

    public static <ID extends Serializable> int deleteByIds(BasicMapper basicMapper, TableInfo tableInfo, Collection<ID> ids) {
        return delete(basicMapper, tableInfo, WhereUtil.create(tableInfo, w -> WhereUtil.appendIdsWhere(w, tableInfo, ids)));
    }

    public static <E> int delete(BasicMapper basicMapper, TableInfo tableInfo, E entity) {
        if (Objects.isNull(entity)) {
            return 0;
        }
        if (tableInfo.getIdFieldInfos().isEmpty()) {
            throw new RuntimeException(tableInfo.getType().getName() + " has no id");
        }
        if (tableInfo.getType() != entity.getClass()) {
            throw new IllegalArgumentException();
        }

        return delete(basicMapper, tableInfo, WhereUtil.create(tableInfo, w -> {
            WhereUtil.appendIdWhereWithEntity(w, tableInfo, entity);
            WhereUtil.appendVersionWhere(w, tableInfo, entity);
        }));
    }

    public static <E> int delete(BasicMapper basicMapper, TableInfo tableInfo, Collection<E> list) {
        if (Objects.isNull(list) || list.isEmpty()) {
            return 0;
        }
        int cnt = 0;
        for (E entity : list) {
            cnt += delete(basicMapper, tableInfo, entity);
        }
        return cnt;
    }

    public static int delete(BasicMapper basicMapper, TableInfo tableInfo, Consumer<Where> consumer) {
        return delete(basicMapper, tableInfo, WhereUtil.create(tableInfo, consumer));
    }

    public static int delete(BasicMapper basicMapper, TableInfo tableInfo, Where where) {
        if (!where.hasContent()) {
            throw new RuntimeException("delete has no where condition content ");
        }
        if (LogicDeleteUtil.isNeedLogicDelete(tableInfo)) {
            //逻辑删除处理
            return LogicDeleteUtil.logicDelete(basicMapper, tableInfo, where);
        }
        Delete delete = new Delete(where);
        delete.delete(tableInfo.getType());
        delete.from(tableInfo.getType());
        return basicMapper.delete(delete);
    }

    public static int deleteAll(BasicMapper basicMapper, TableInfo tableInfo) {
        return delete(basicMapper, tableInfo, where -> where.eq(SQL1.INSTANCE, 1));
    }
}
