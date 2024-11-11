package cn.mybatis.mp.core.mybatis.mapper.mappers;

import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;

public interface BaseMapper<T> {

    /**
     * 获取当前实体类
     *
     * @return Class
     */
    Class<T> getEntityType();

    /**
     * 获取当前实体类的TableInfo
     *
     * @return
     */
    TableInfo getTableInfo();

    /**
     * 获取基础Mapper
     *
     * @return BasicMapper
     */
    BasicMapper getBasicMapper();
}
