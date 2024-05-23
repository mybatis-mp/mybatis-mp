package com.mybatis.mp.core.test.mapper;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.db.annotations.Paging;
import com.mybatis.mp.core.test.DO.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysRoleMapper extends MybatisMapper<SysRole> {
    @Paging
    Pager<SysRole> xmlPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2);

    @Paging
    Pager<SysRole> xmlPaging2(Pager<SysRole> pager);

    @Paging
    Pager<SysRole> xmlDynamicPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2, @Param("id3") Integer id3);

    @Paging
    @Select("select * from sys_role where id >=#{id} and id <=#{id2} order by id asc")
    Pager<SysRole> annotationPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2);
}
