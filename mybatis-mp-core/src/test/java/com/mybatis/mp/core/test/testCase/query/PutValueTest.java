package com.mybatis.mp.core.test.testCase.query;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.SysUser;
import com.mybatis.mp.core.test.mapper.SysUserMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.vo.PutValueEnum;
import com.mybatis.mp.core.test.vo.PutValueVo;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PutValueTest extends BaseTest {

    @Test
    public void putEnumValueTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<PutValueVo> list = QueryChain.of(sysUserMapper)
                    .orderBy(SysUser::getId)
                    .returnType(PutValueVo.class)
                    .list();
            list.stream().forEach(System.out::println);
            assertEquals(list.get(0).getEnumName(), PutValueEnum.ENUM1.getName());
            assertEquals(list.get(1).getEnumName(), PutValueEnum.ENUM2.getName());
        }
    }

    @Test
    public void putEnumDefaultValueTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            PutValueVo vo = QueryChain.of(sysUserMapper)
                    .eq(SysUser::getId, 3)
                    .returnType(PutValueVo.class)
                    .get();
            System.out.println(vo);
            assertNull(vo.getEnumName());
            assertEquals(vo.getDefaultEnumName(), "NULL");
        }
    }

    @Test
    public void putValueTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            List<PutValueVo> list = QueryChain.of(sysUserMapper)
                    .orderBy(SysUser::getId)
                    .returnType(PutValueVo.class)
                    .list();
            list.stream().forEach(System.out::println);
            assertEquals(list.get(0).getEnumName(), PutValueEnum.ENUM1.getName());
            assertEquals(list.get(1).getEnumName(), PutValueEnum.ENUM2.getName());
        }
    }

    @Test
    public void putDefaultValueTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            SysUserMapper sysUserMapper = session.getMapper(SysUserMapper.class);
            PutValueVo vo = QueryChain.of(sysUserMapper)
                    .eq(SysUser::getId, 3)
                    .returnType(PutValueVo.class)
                    .get();
            System.out.println(vo);
            assertNull(vo.getEnumName());
            assertEquals(vo.getDefaultEnumName(), "NULL");
        }
    }
}
