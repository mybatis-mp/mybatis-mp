package com.mybatis.mp.core.test.model;

import cn.mybatis.mp.db.Model;
import cn.mybatis.mp.db.annotations.ModelEntityField;
import com.mybatis.mp.core.test.DO.IdTest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IdTestModel implements Model<IdTest> {

    @ModelEntityField("id")
    private Long xxid;

    @ModelEntityField("createTime")
    private LocalDateTime createTime2;
}
