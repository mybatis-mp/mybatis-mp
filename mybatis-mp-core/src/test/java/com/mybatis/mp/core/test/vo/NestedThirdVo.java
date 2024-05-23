package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.NestedResultEntity;
import com.mybatis.mp.core.test.DO.NestedThird;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

@Data
public class NestedThirdVo {

    private Integer id;

    private Integer nestedSecondId;

    private String thName;

    @NestedResultEntity(target = NestedThird.class)
    private NestedThird nestedThird;

    @Fetch(source = NestedThird.class, property = "id", target = SysRole.class, targetProperty = "id")
    private SysRoleVo sysRole;

    @Fetch(source = NestedThird.class, property = "id", target = SysUser.class, targetProperty = "id")
    private SysUser sysUser;
}
