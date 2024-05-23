package com.mybatis.mp.core.test.vo;

import cn.mybatis.mp.db.annotations.NestedResultEntity;
import cn.mybatis.mp.db.annotations.ResultEntity;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.DO.SysUserScore;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ResultEntity(SysUserScore.class)
public class SysUserScoreVo {

    private Integer userId;

    private BigDecimal score;

    @NestedResultEntity(target = SysUser.class)
    private SysUserVo sysUserVo;

    @NestedResultEntity(target = SysRole.class)
    private SysRole sysRole;
}
