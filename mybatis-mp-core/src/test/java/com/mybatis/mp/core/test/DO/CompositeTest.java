package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Setter
@Getter
@Table
@SuperBuilder
@NoArgsConstructor
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
