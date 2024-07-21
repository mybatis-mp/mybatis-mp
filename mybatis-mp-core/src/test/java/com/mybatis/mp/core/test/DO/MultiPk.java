package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Table
@EqualsAndHashCode
public class MultiPk {

    @TableId(IdAutoType.NONE)
    private Integer id1;

    @TableId(IdAutoType.NONE)
    private Integer id2;

    private String name;
}
