package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ResultEntity(SysUser.class)
public class FetchSysUserForRoleVo {

    private Integer id;

    private String userName;

    private String password;

    private Integer role_id;

    private LocalDateTime create_time;

}
