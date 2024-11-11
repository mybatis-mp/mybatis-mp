package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.PutValue;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.GetPutValueFactory;
import com.mybatis.mp.core.test.GetPutValueFactory2;
import lombok.Data;

@Data
@ResultEntity(SysUser.class)
public class PutValueVo {

    private Integer id;

    @PutValue(source = SysUser.class, property = "id", factory = GetPutValueFactory.class, method = "getPutValue1")
    private String enumName;

    @PutValue(source = SysUser.class, property = "id", factory = GetPutValueFactory.class, method = "getPutValue1", defaultValue = "NULL")
    private String defaultEnumName;

    @PutValue(source = SysUser.class, property = "id", factory = GetPutValueFactory2.class, method = "getPutValue2")
    private String enumName2;

    @PutValue(source = SysUser.class, property = "id", factory = GetPutValueFactory2.class, method = "getPutValue2", defaultValue = "NULL")
    private String defaultEnumName2;
}

