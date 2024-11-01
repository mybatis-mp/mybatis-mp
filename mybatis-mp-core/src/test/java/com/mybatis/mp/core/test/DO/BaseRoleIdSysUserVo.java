package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.TableId;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ResultEntity(BaseIDSysUser.class)
public class BaseRoleIdSysUserVo extends BaseRoleId<String> {

    @TableId
    private Long id;

    private String password;

    private LocalDateTime create_time;

    @Fetch(source = BaseIDSysUser.class, property = "id", target = BaseIDSysUser.class, targetProperty = "id")
    private BaseIDSysUser baseIDSysUser;

    @NestedResultEntity(target = BaseIDSysUser.class)
    private BaseIDSysUser baseIDSysUser2;


    @Fetch(source = BaseIDSysUser.class, property = "id", target = BaseIDSysUser.class, targetProperty = "id")
    private List<BaseIDSysUser> baseIDSysUsers;

    @Fetch(source = BaseIDSysUser.class, property = "id", target = BaseIDSysUser.class, targetProperty = "id")
    private BaseRoleIdSysUserVo2 baseRoleIdSysUserVo;

    @NestedResultEntity(target = BaseIDSysUser.class)
    private BaseRoleIdSysUserVo2 baseRoleIdSysUserVo2;
}
