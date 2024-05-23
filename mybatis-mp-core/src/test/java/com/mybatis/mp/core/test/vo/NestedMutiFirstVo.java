package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.NestedMutiFirst;
import com.mybatis.mp.core.test.DO.NestedMutiSecond;
import lombok.Data;

import java.util.List;

@Data
@ResultEntity(NestedMutiFirst.class)
public class NestedMutiFirstVo {

    @NestedResultEntity(target = NestedMutiFirst.class)
    private NestedMutiFirst nestedFirst;

    private Integer id;

    private String thName;

    @NestedResultEntity(target = NestedMutiSecond.class)
    private List<NestedMutiSecondVo> nestedSecondVo;
}
