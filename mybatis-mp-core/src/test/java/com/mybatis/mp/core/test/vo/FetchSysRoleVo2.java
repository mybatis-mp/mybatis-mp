package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Fetch;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

import java.util.List;

@Data
@ResultEntity(SysRole.class)
public class FetchSysRoleVo2 {

    private Integer id;

    private String name;


    @Fetch(column = "id", target = SysUser.class, targetProperty = "role_id", targetSelectProperty = "userName", orderBy = "id asc")
    private List<String> sysRoleNames;


    @Fetch(column = "id", target = SysUser.class, targetProperty = "role_id", targetSelectProperty = "[count({id})]")
    private Integer cnts;

    @Fetch(column = "id", target = SysRole.class, targetProperty = "id", targetSelectProperty = "name")
    private String roleName;

    @Fetch(column = "id", target = SysUser.class, targetProperty = "role_id", targetSelectProperty = "[count({id})]")
    private Integer cnts2;

    @Fetch(column = "id", target = SysUser.class, targetProperty = "role_id", targetSelectProperty = "[count({id})]", groupBy = "role_id", forceUseIn = true)
    private Integer cnts3;
}
