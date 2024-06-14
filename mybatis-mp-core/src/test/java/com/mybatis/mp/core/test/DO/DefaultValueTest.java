package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableField;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import lombok.Data;

import java.time.LocalDateTime;

@Table
@Data
public class DefaultValueTest {

    @TableId
    @TableId(dbType = DbType.ORACLE, value = IdAutoType.SQL, sql = "select default_value_test_seq.NEXTVAL FROM dual")
    @TableId(dbType = DbType.KING_BASE, value = IdAutoType.SQL, sql = "select default_value_test_seq.NEXTVAL FROM dual")
    private Integer id;

    @TableField(defaultValue = "{BLANK}")
    private String value1;

    @TableField(defaultValue = "1", updateDefaultValue = "2")
    private Integer value2;

    @TableField(defaultValue = "{NOW}")
    private LocalDateTime createTime;

    private TestEnum value3;
}
