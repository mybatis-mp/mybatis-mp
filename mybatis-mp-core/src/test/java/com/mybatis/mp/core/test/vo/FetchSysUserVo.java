package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ResultEntity(SysUser.class)
public class FetchSysUserVo {

    private Integer id;

    private String userName;

    private String password;

    @Fetch(source = SysUser.class, property = "role_id", target = SysRole.class, targetProperty = "id")
    private SysRoleVo sysRole;

    private LocalDateTime create_time;

}
