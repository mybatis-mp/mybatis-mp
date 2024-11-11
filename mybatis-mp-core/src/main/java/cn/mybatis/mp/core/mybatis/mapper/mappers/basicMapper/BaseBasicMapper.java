package cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper;

import cn.mybatis.mp.core.mybatis.mapper.BasicMapper;
import cn.mybatis.mp.core.mybatis.mapper.mappers.ProviderMapper;

public interface BaseBasicMapper extends ProviderMapper {

    default BasicMapper getBasicMapper() {
        return (BasicMapper) this;
    }
}
