package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.CountMethodUtil;
import db.sql.api.impl.cmd.struct.Where;

import java.util.function.Consumer;

public interface CountMapper<T> extends BaseMapper<T> {

    /**
     * 总数
     *
     * @return count数
     */
    default int countAll() {
        return CountMethodUtil.countAll(getBasicMapper(), getTableInfo());
    }

    /**
     * 是否存在
     *
     * @param consumer where consumer
     * @return count数
     */
    default int count(Consumer<Where> consumer) {
        return CountMethodUtil.count(getBasicMapper(), getTableInfo(), consumer);
    }

    /**
     * 是否存在
     *
     * @param where
     * @return count数
     */
    default int count(Where where) {
        return CountMethodUtil.count(getBasicMapper(), getTableInfo(), where);
    }
}
