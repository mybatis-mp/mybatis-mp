package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import lombok.Data;

@Data
@ResultEntity(SysRole.class)
public class SysRoleVo {

    private Integer id;

    private String name;

    @Fetch(property = "id", source = SysRole.class, targetProperty = "id", target = SysRole.class)
    private SysRole sysRole;
}
