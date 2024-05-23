package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntityField;
import cn.mybatis.mp.db.annotations.TenantId;
import com.mybatis.mp.core.test.DO.NestedFirst;
import com.mybatis.mp.core.test.DO.NestedSecond;
import com.mybatis.mp.core.test.DO.NestedThird;
import lombok.Data;

@Data
public class NestedSecondVo {

    @TenantId
    private Integer id;

    private Integer nestedOneId;

    private String thName;

    @ResultEntityField(target = NestedFirst.class, property = "thName")
    private String thName2;

    @NestedResultEntity(target = NestedSecond.class)
    private NestedSecond nestedSecond;


    @NestedResultEntity(target = NestedThird.class)
    private NestedThirdVo nestedThirdVo;
}
