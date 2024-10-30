package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.*;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.OnValueFactory;
import lombok.Data;

@Data
@ResultEntity(SysUser.class)
@OnValue(OnValueFactory.class)
public class OnValueVo {

    private String id;

    private String userName;

    @NestedResultEntity(target = SysRole.class)
    private OnValueNestedVo onValueNestedVo;

    @Fetch(source = SysUser.class, property = "role_id", target = SysRole.class, targetProperty = "id")
    private OnValueFetchVo onValueFetchVo;

    @Ignore
    private String sourcePut;
}
