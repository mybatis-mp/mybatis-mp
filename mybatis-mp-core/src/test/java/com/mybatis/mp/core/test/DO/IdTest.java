package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.IdAutoType;
import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableId;
import db.sql.api.DbType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table
public class IdTest {


    @TableId
    @TableId(dbType = DbType.H2, value = IdAutoType.AUTO)
    @TableId(dbType = DbType.SQL_SERVER, value = IdAutoType.AUTO)
    @TableId(dbType = DbType.PGSQL, value = IdAutoType.SQL, sql = "select nextval('id_test_id_seq')")
    @TableId(dbType = DbType.ORACLE, value = IdAutoType.SQL, sql = "select id_test_seq.NEXTVAL FROM dual")
    @TableId(dbType = DbType.KING_BASE, value = IdAutoType.SQL, sql = "select id_test_seq.NEXTVAL FROM dual")
    private Long id;

    private LocalDateTime createTime;

}
