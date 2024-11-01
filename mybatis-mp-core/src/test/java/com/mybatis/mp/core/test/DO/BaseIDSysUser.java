package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.ForeignKey;
import cn.mybatis.mp.db.annotations.Table;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString(callSuper = true)
@Table("t_sys_user")
public class BaseIDSysUser extends BaseId<Long> {

    private String userName;

    private String password;

    @ForeignKey(SysRole.class)
    private Integer role_id;

    private LocalDateTime create_time;


}
