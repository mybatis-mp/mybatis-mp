package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.ResultField;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ResultEntity(SysRole.class)
public class OneToManyVo {

    private Integer id;

    private String name;

    private LocalDateTime createTime;

    @ResultField(value = "otherName")
    private String asName;

    @NestedResultEntity(target = SysUser.class)
    private List<SysUser> sysUserList;
}
