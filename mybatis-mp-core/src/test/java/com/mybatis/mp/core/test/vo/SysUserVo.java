package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.*;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

@Data
@ResultEntity(SysUser.class)
public class SysUserVo {

    private Integer id;

    @ResultEntityField
    private String userName;

    @NestedResultEntityField("password")
    @ResultEntityField(target = SysUser.class, property = "password")
    private String pwd;

    @ResultField("kk")
    private String kkName;

    @ResultField
    private String kkName2;

    @NestedResultEntity(target = SysRole.class)
    private SysRole role;
}
