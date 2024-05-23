package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

import java.util.List;

@Data
@ResultEntity(SysUser.class)
public class NestedMutiVo {

    private Integer id;

    private String userName;

    private String password;


    private Integer role_id;

    @NestedResultEntity(target = SysRole.class)
    private List<NestedMutiSysRoleVo> sysUserList;

    @Fetch(source = SysUser.class, property = "id", target = SysRole.class, targetProperty = "id")
    private SysRoleVo sysRole;
}
