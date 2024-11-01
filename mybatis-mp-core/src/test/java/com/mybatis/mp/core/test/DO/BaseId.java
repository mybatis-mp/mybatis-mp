package com.mybatis.mp.core.test.DO;


import cn.mybatis.mp.db.annotations.Ignore;
import cn.mybatis.mp.db.annotations.TableId;
import lombok.Data;

import java.util.List;

@Data
public class BaseId<ID> {

    @TableId
    private ID id;

    @Ignore
    private List<ID> ids;
}
