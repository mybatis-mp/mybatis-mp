package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.*;
import db.sql.api.DbType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table
public class CompositeTest {

    @TableId
    @TableId(dbType = DbType.ORACLE, value = IdAutoType.SQL, sql = "select composite_test_seq.NEXTVAL FROM dual")
    @TableId(dbType = DbType.KING_BASE, value = IdAutoType.SQL, sql = "select composite_test_seq.NEXTVAL FROM dual")
    private Long id;

    @Version
    private Integer version;

    @TenantId
    private Integer tenantId;

    private LocalDateTime deleteTime;

    @TableField(defaultValue = "0")
    @LogicDelete(beforeValue = "0", afterValue = "1", deleteTimeField = "deleteTime")
    private Byte deleted;
}
