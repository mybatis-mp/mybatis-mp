package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

@Data
public class NestedMutiSysRoleVo {

    private Integer id;

    private String name;

    @Fetch(source = SysRole.class, property = "id", target = SysRole.class, targetProperty = "id")
    private SysRoleVo sysRole;

    @Fetch(source = SysRole.class, property = "id", target = SysUser.class, targetProperty = "id")
    private SysUser sysUser;

}
