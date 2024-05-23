package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.NestedFirst;
import com.mybatis.mp.core.test.DO.NestedSecond;
import lombok.Data;

@Data
@ResultEntity(NestedFirst.class)
public class NestedFirstVo {

    @NestedResultEntity(target = NestedFirst.class)
    private NestedFirst nestedFirst;

    private Integer id;

    private String thName;

    @NestedResultEntity(target = NestedSecond.class)
    private NestedSecondVo nestedSecondVo;
}
