package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.ResultField;
import cn.mybatis.mp.db.annotations.TypeHandler;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.typeHandler.PhoneTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ResultEntity(SysRole.class)
public class OneToManyTypeHandlerVo {

    private Integer id;

    @TypeHandler(PhoneTypeHandler.class)
    private String name;

    private LocalDateTime createTime;

    @TypeHandler(PhoneTypeHandler.class)
    @ResultField("kk2")
    private String kkName;

    @NestedResultEntity(target = SysUser.class)
    private List<SysUserTypeHandlerVo> sysUserList;
}
