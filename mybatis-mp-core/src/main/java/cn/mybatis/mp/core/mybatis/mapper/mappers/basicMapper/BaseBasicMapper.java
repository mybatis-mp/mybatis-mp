package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.BaseMapper;

public interface BaseBasicMapper extends BaseMapper {

    default BasicMapper getBasicMapper() {
        return (BasicMapper) this;
    }
}
