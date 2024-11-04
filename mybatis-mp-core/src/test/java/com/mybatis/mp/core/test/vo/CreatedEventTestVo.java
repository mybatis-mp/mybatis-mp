package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.*;
import com.mybatis.mp.core.test.CreatedEventFactory;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import lombok.Data;

@Data
@ResultEntity(SysUser.class)
@CreatedEvent(CreatedEventFactory.class)
public class CreatedEventTestVo {

    private String id;

    private String userName;

    @NestedResultEntity(target = SysRole.class)
    private CreatedEventNestedTestVo createdEventNestedTestVo;

    @Fetch(source = SysUser.class, property = "role_id", target = SysRole.class, targetProperty = "id")
    private CreatedEventFetchTestVo createdEventFetchTestVo;

    @Ignore
    private String sourcePut;
}
