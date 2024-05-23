package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.core.incrementer.IdentifierGeneratorType;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableField;
import cn.mybatis.mp.db.annotations.TableId;
import cn.mybatis.mp.db.annotations.TenantId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table
public class TenantTest {

    @TableId(value = IdAutoType.GENERATOR, generatorName = IdentifierGeneratorType.UUID)
    private String id;

    @TenantId
    private Integer tenantId;

    private String name;

    @TableField(update = false)
    private LocalDateTime createTime;
}
