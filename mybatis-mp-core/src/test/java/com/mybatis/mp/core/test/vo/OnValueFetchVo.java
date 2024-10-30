package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.Ignore;
import cn.mybatis.mp.db.annotations.OnValue;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.OnValueFactory;
import lombok.Data;

@Data
@ResultEntity(SysRole.class)
@OnValue(OnValueFactory.class)
public class OnValueFetchVo {

    private String id;

    private String name;

    @Ignore
    private String sourcePut;
}
