package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.core.incrementer.IdentifierGeneratorType;
import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("uuid_test")
public class UUIDTest {

    @TableId(value = IdAutoType.GENERATOR, generatorName = IdentifierGeneratorType.UUID)
    private String id;

    private LocalDateTime createTime;
}
