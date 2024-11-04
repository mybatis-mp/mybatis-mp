package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysRole;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.vo.CreatedEventTestVo;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreatedEventTest extends BaseTest {


    @Test
    public void createdEventTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<CreatedEventTestVo> list = QueryChain.of(sysUserMapper)
                    .from(SysUser.class)
                    .join(SysUser.class, SysRole.class)
                    .returnType(CreatedEventTestVo.class)
                    .list();

            list.forEach(item -> {
                assertEquals(item.getSourcePut(), "CreatedEventTestVo");
                assertEquals(item.getCreatedEventNestedTestVo().getSourcePut(), "CreatedEventNestedTestVo");
                assertEquals(item.getCreatedEventFetchTestVo().getSourcePut(), "CreatedEventFetchTestVo");
            });

        }
    }
}
