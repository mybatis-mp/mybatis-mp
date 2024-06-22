package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.core.mybatis.typeHandler.JacksonTypeHandler;
import cn.mybatis.mp.db.annotations.ResultEntity;
import cn.mybatis.mp.db.annotations.ResultField;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

import java.util.List;

@Data
@ResultEntity(SysUser.class)
public class JsonTypeTestVo {

    @ResultField(value = "aa", typeHandler = JacksonTypeHandler.class)
    private List<SysUser> aa;

    @ResultField(value = "bb", typeHandler = JacksonTypeHandler.class)
    private SysUser bb;

    @ResultField(value = "aa", typeHandler = JacksonTypeHandler.class)
    private List<SysRole> dd;

    @ResultField(value = "bb", typeHandler = JacksonTypeHandler.class)
    private SysRole ee;
}
