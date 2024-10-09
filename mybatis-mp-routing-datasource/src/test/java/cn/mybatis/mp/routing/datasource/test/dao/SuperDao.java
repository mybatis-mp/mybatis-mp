package cn.mybatis.mp.routing.datasource.test.dao;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;

import java.io.Serializable;

public interface SuperDao<T> {

    MybatisMapper<T> getMapper();

    default T getById2(Serializable id){
        return getMapper().getById(id);
    }
}
