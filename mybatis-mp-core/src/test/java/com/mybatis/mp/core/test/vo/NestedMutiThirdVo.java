package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.NestedResultEntity;
import com.mybatis.mp.core.test.DO.NestedMutiThird;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

@Data
public class NestedMutiThirdVo {

    private Integer id;

    private Integer nestedSecondId;

    private String thName;

    @NestedResultEntity(target = NestedMutiThird.class)
    private NestedMutiThird nestedThird;

    @Fetch(source = NestedMutiThird.class, property = "id", target = SysRole.class, targetProperty = "id")
    private SysRoleVo sysRole;

    @Fetch(source = NestedMutiThird.class, property = "id", target = SysUser.class, targetProperty = "id")
    private SysUser sysUser;
}
