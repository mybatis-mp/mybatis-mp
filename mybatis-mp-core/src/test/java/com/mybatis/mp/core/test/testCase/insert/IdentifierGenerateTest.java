package com.mybatis.mp.core.test.testCase.insert;

import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.IdTest;
import com.mybatis.mp.core.test.DO.IdTest2;
import com.mybatis.mp.core.test.DO.UUIDTest;
import com.mybatis.mp.core.test.mapper.IdTest2Mapper;
import com.mybatis.mp.core.test.mapper.IdTestMapper;
import com.mybatis.mp.core.test.mapper.UUIDMapper;
import com.mybatis.mp.core.test.model.IdTestModel;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import db.sql.api.DbType;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class IdentifierGenerateTest extends BaseTest {

    @Test
    public void insertTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            IdTestMapper idTestMapper = session.getMapper(IdTestMapper.class);
            IdTest idTest = new IdTest();
            idTest.setCreateTime(LocalDateTime.now());
            idTestMapper.save(idTest);
            assertNotNull(idTest.getId());
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            IdTestMapper idTestMapper = session.getMapper(IdTestMapper.class);
            IdTest idTest = new IdTest();
            idTest.setCreateTime(LocalDateTime.now());
            idTestMapper.getBasicMapper().save(idTest);
            //System.out.println(idTest);
            assertNotNull(idTest.getId());
        }
    }

    @Test
    public void insertModelTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            IdTestMapper idTestMapper = session.getMapper(IdTestMapper.class);
            IdTestModel idTest = new IdTestModel();
            idTest.setCreateTime2(LocalDateTime.now());
            idTestMapper.save(idTest);
            //System.out.println(idTest);
            assertNotNull(idTest.getXxid());
        }

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            IdTestMapper idTestMapper = session.getMapper(IdTestMapper.class);
            IdTestModel idTest = new IdTestModel();
            idTest.setCreateTime2(LocalDateTime.now());
            idTestMapper.getBasicMapper().save(idTest);
            //System.out.println(idTest);
            assertNotNull(idTest.getXxid());
        }
    }

    @Test
    public void insertWithIdTest() {
        if (TestDataSource.DB_TYPE == DbType.SQL_SERVER || TestDataSource.DB_TYPE == DbType.DB2) {
            //sql server 不支持自增ID插入
            return;
        }
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            IdTestMapper idTestMapper = session.getMapper(IdTestMapper.class);
            IdTest idTest = new IdTest();
            idTest.setId(1L);
            idTest.setCreateTime(LocalDateTime.now());
            idTestMapper.save(idTest);
            System.out.println(idTest);
            assertEquals(1L, (long) idTest.getId());

            assertNotNull(idTestMapper.getById(idTest.getId()));
        }
    }

    @Test
    public void insertWithId2Test() {
        if (TestDataSource.DB_TYPE == DbType.SQL_SERVER || TestDataSource.DB_TYPE == DbType.DB2) {
            //sql server 不支持自增ID插入
            return;
        }
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            IdTest2Mapper idTestMapper = session.getMapper(IdTest2Mapper.class);
            IdTest2 idTest = new IdTest2();
            idTest.setId(1L);
            idTest.setCreateTime(LocalDateTime.now());
            idTestMapper.save(idTest);
            System.out.println(idTest);
            assertEquals(1L, (long) idTest.getId());


            assertNotNull(QueryChain.of(idTestMapper).eq(IdTest2::getId, 1).get());
        }
    }

    @Test
    public void insertUUIDTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            UUIDMapper idTestMapper = session.getMapper(UUIDMapper.class);
            UUIDTest idTest = new UUIDTest();
            idTest.setCreateTime(LocalDateTime.now());
            idTestMapper.save(idTest);
            assertNotNull(idTest.getId());
        }
    }
}
