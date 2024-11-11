package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.CreatedEvent;
import cn.mybatis.mp.db.annotations.Ignore;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.CreatedEventFactory2;
import com.mybatis.mp.core.test.DO.SysRole;
import lombok.Data;

@Data
@ResultEntity(SysRole.class)
@CreatedEvent(CreatedEventFactory2.class)
public class CreatedEventFetchTestVo {

    private String id;

    private String name;

    @Ignore
    private String sourcePut;
}
