package com.mybatis.mp.core.test.vo;


import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

@Data
@ResultEntity(SysUser.class)
public class FetchSysRoleVoWhenHasNested {

    @Fetch(source = SysUser.class, property = "role_id", target = SysRole.class, targetProperty = "id")
    private SysRole sysRole;

    @NestedResultEntity(target = SysUser.class)
    private SysUser sysUser;
}
