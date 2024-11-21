/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.mybatis.mp.core.test.mapper;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.db.annotations.Paging;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.vo.JsonTypeTestVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface SysRoleMapper extends MybatisMapper<SysRole> {
    @Paging(optimize = false)
    Pager<SysRole> xmlPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2);

    @Paging
    Pager<SysRole> xmlPaging2(Pager<SysRole> pager);

    @Paging
    Pager<SysRole> xmlDynamicPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2, @Param("id3") Integer id3);

    @Paging
    @Select("select * from sys_role where id >=#{id} and id <=#{id2} order by id asc")
    Pager<SysRole> annotationPaging(Pager<SysRole> pager, @Param("id") Integer id, @Param("id2") Integer id2);


    JsonTypeTestVo jsonTypeTest1(@Param("sql") String sql);

    JsonTypeTestVo jsonTypeTest2(@Param("sql") String sql);

}
