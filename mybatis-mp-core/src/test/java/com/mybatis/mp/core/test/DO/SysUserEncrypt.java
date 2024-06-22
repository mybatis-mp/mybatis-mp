package com.mybatis.mp.core.test.DO;

import cn.mybatis.mp.db.annotations.ForeignKey;
import cn.mybatis.mp.db.annotations.Table;
import cn.mybatis.mp.db.annotations.TableField;
import cn.mybatis.mp.db.annotations.TableId;
import com.mybatis.mp.core.test.typeHandler.EncryptTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Table("t_sys_user")
public class SysUserEncrypt {

    @TableId
    @TableField("id")
    private Integer id;

    @TableField(typeHandler = EncryptTypeHandler.class)
    private String userName;

    private String password;

    @ForeignKey(SysRole.class)
    private Integer role_id;

    private LocalDateTime create_time;
}
