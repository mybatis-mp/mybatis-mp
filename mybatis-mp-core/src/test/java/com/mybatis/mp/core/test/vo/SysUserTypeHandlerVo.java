package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntityField;
import cn.mybatis.mp.db.annotations.ResultField;
import cn.mybatis.mp.db.annotations.TypeHandler;
import com.mybatis.mp.core.test.typeHandler.PhoneTypeHandler;
import lombok.Data;

@Data
public class SysUserTypeHandlerVo {

    private Integer id;

    @TypeHandler(PhoneTypeHandler.class)
    private String userName;

    @TypeHandler(PhoneTypeHandler.class)
    @NestedResultEntityField("password")
    private String pwd;

    @TypeHandler(PhoneTypeHandler.class)
    @ResultField("kk")
    private String kkName;

}
