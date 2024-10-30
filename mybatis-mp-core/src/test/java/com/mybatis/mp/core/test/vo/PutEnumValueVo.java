package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Ignore;
import cn.mybatis.mp.db.annotations.PutEnumValue;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

@Data
@ResultEntity(SysUser.class)
public class PutEnumValueVo {

    private Integer id;

    @PutEnumValue(source = SysUser.class, property = "id", target = PutValueEnum.class)
    private String enumName;

    @PutEnumValue(source = SysUser.class, property = "id", target = PutValueEnum.class, defaultValue = "NULL")
    private String defaultEnumName;

    @Ignore
    private String sourcePut;
}
