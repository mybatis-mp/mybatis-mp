package cn.mybatis.mp.routing.datasource.test.DO;

import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableId;

@Table
public class Hikari {

    @TableId
    private Integer id;

    private String hikariName;

}
