package com.mybatis.mp.core.test.model;

import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.ForeignKey;
import com.mybatis.mp.core.test.DO.BaseIDSysUser;
import com.mybatis.mp.core.test.DO.BaseId;
import com.mybatis.mp.core.test.DO.SysRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseIDSysUserModel extends BaseId<Long> implements Model<BaseIDSysUser> {

    private String userName;

    private String password;

    @ForeignKey(SysRole.class)
    private Integer role_id;

    private LocalDateTime create_time;
}
