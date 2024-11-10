package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.db.reflect.TableInfo;

public interface BaseMybatisMapper<T> {
    /**
     * 获取实体类的type
     *
     * @return 当前实体类Class
     */
    Class<T> getEntityType();

    /**
     * 获取实体类信息
     *
     * @return TableInfo
     */
    TableInfo getTableInfo();
}
