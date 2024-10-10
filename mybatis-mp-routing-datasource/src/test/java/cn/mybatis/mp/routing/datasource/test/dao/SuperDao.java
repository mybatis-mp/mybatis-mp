package cn.mybatis.mp.routing.datasource.test.dao;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;

@Transactional(propagation = Propagation.REQUIRES_NEW)
public interface SuperDao<T> {

    MybatisMapper<T> getMapper();

    default T getById2(Serializable id){
        return getMapper().getById(id);
    }
}
