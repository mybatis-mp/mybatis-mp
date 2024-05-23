package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.TenantId;
import com.mybatis.mp.core.test.DO.NestedMutiSecond;
import com.mybatis.mp.core.test.DO.NestedMutiThird;
import lombok.Data;

import java.util.List;

@Data
public class NestedMutiSecondVo {

    @TenantId
    private Integer id;

    private Integer nestedOneId;

    private String thName;

    @NestedResultEntity(target = NestedMutiSecond.class)
    private NestedMutiSecond nestedSecond;


    @NestedResultEntity(target = NestedMutiThird.class)
    private List<NestedMutiThirdVo> nestedThirdVo;
}
