package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

import java.util.List;

@Data
@ResultEntity(SysRole.class)
public class FetchSysRoleVo {

    private Integer id;

    private String name;

    @Fetch(column = "id", target = SysUser.class, targetProperty = "role_id", targetSelectProperty = "userName", orderBy = "id asc")
    private List<String> sysRoleNames;

    @Fetch(column = "id", target = SysUser.class, targetProperty = "role_id", orderBy = "[{id} asc,{userName} desc]")
    private List<FetchSysUserForRoleVo> sysRole;
}
