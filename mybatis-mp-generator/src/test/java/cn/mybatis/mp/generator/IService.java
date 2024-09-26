package cn.mybatis.mp.generator;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;

public interface IService<T,ID> {

    T getById(ID id);

    int deleteById(ID id);
}
