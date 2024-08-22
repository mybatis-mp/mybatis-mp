package com.mybatis.mp.core.test.mapper;


import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import com.mybatis.mp.core.test.DO.SysUser;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.mapping.ResultSetType;

import java.util.List;
import java.util.Map;

public interface SysUserMapper extends MybatisMapper<SysUser> {

    @Select("select * from big_data limit 1000000")
    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    List<Map> selectAll();

    @Select("select * from big_data limit 1000000")
    List<Map> selectAll2();

    @Options(resultSetType = ResultSetType.FORWARD_ONLY, fetchSize = 1000)
    @Select("select * from big_data limit 1000000")
    Cursor<Map> selectAll3();
}
