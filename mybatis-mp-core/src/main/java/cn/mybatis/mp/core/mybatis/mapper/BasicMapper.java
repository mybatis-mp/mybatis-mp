package cn.mybatis.mp.core.mybatis.mapper;


import cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper.*;

public interface BasicMapper extends GetBasicMapper, ExistsBasicMapper, CountBasicMapper, ListBasicMapper, CursorBasicMapper,
        PagingBasicMapper, MapWithKeyBasicMapper, SaveBasicMapper, SaveOrUpdateBasicMapper, SaveModelBasicMapper, SaveOrUpdateModelBasicMapper,
        UpdateBasicMapper, UpdateModelBasicMapper, DeleteBasicMapper {

}
