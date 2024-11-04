package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@Table
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CompositeTest extends BaseEntity {

    @Version
    private Integer version;

    @TenantId
    private Integer tenantId;

    private LocalDateTime deleteTime;

    @TableField(defaultValue = "0")
    @LogicDelete(beforeValue = "0", afterValue = "1", deleteTimeField = "deleteTime")
    private Byte deleted;
}
