package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.TableId;
import cn.mybatis.mp.db.annotations.Version;
import db.sql.api.DbType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseEntity {

    @TableId
    @TableId(dbType = DbType.ORACLE, value = IdAutoType.SQL, sql = "select composite_test_seq.NEXTVAL FROM dual")
    @TableId(dbType = DbType.KING_BASE, value = IdAutoType.SQL, sql = "select composite_test_seq.NEXTVAL FROM dual")
    private Long id;

    @Version
    private Integer version;
}
