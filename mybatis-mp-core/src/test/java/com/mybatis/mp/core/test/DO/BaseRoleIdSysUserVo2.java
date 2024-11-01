package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ResultEntity(BaseIDSysUser.class)
public class BaseRoleIdSysUserVo2 extends BaseRoleId<String> {

    @TableId
    private Long id;

    private String password;

    private LocalDateTime create_time;


}
