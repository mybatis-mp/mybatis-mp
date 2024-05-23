package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableId;
import lombok.Data;

@Data
@Table
public class NestedFirst {

    @TableId
    private Integer id;

    private String thName;

}
