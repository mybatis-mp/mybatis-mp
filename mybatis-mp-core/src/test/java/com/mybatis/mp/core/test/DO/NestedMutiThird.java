package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableId;
import lombok.Data;

@Data
@Table
public class NestedMutiThird {

    @TableId
    private Integer id;

    private Integer nestedSecondId;

    private String thName;

}
