package com.mybatis.mp.core.test.testCase.insert;

import cn.mybatis.mp.core.sql.executor.Query;
import cn.mybatis.mp.core.sql.executor.chain.InsertChain;
import cn.mybatis.mp.core.sql.executor.chain.QueryChain;
import com.mybatis.mp.core.test.DO.DefaultValueTest;
import com.mybatis.mp.core.test.DO.TestEnum;
import com.mybatis.mp.core.test.mapper.DefaultValueTestMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import com.mybatis.mp.core.test.testCase.TestDataSource;
import db.sql.api.DbType;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultValueTestCase extends BaseTest {

    @Test
    public void defaultValueTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            DefaultValueTestMapper mapper = session.getMapper(DefaultValueTestMapper.class);
            DefaultValueTest defaultValueTest = new DefaultValueTest();
            defaultValueTest.setValue3(TestEnum.X1);
            DbType.H2.getKeywords().add("value3");
            mapper.save(defaultValueTest);

            System.out.println(defaultValueTest);
            assertNotNull(defaultValueTest.getId());
            assertNotNull(defaultValueTest.getValue1());
            assertNotNull(defaultValueTest.getValue2());

            assertNotNull(defaultValueTest.getCreateTime());
            defaultValueTest = mapper.getById(defaultValueTest.getId());
            System.out.println(defaultValueTest);
            assertEquals(TestEnum.X1, defaultValueTest.getValue3());

            QueryChain.of(mapper)
                    .in(DefaultValueTest::getValue1, Arrays.asList(TestEnum.X1, TestEnum.X2)).list();
        }
    }


    @Test
    public void updateValueTest() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            DefaultValueTestMapper mapper = session.getMapper(DefaultValueTestMapper.class);
            DefaultValueTest defaultValueTest = new DefaultValueTest();
            defaultValueTest.setValue3(TestEnum.X1);


            mapper.save(defaultValueTest);
            defaultValueTest.setValue2(null);
            mapper.update(defaultValueTest);

            assertEquals(2, defaultValueTest.getValue2());
            defaultValueTest = mapper.getById(defaultValueTest.getId());
            assertEquals(2, defaultValueTest.getValue2());

        }
    }

    @Test
    public void batchInsert() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            DefaultValueTestMapper mapper = session.getMapper(DefaultValueTestMapper.class);
            DefaultValueTest defaultValueTest = new DefaultValueTest();
            DefaultValueTest defaultValueTest2 = new DefaultValueTest();
            DefaultValueTest defaultValueTest3 = new DefaultValueTest();

            List<DefaultValueTest> list = Arrays.asList(defaultValueTest, defaultValueTest2, defaultValueTest3);

            if (TestDataSource.DB_TYPE == DbType.ORACLE || TestDataSource.DB_TYPE == DbType.KING_BASE) {
                defaultValueTest.setId(11);
                defaultValueTest2.setId(12);
                defaultValueTest3.setId(13);
                mapper.saveBatch(list, DefaultValueTest::getId, DefaultValueTest::getValue1, DefaultValueTest::getValue2, DefaultValueTest::getCreateTime);
            } else {
                mapper.saveBatch(list, DefaultValueTest::getValue1, DefaultValueTest::getValue2, DefaultValueTest::getCreateTime);
            }

            if (TestDataSource.DB_TYPE == DbType.ORACLE || TestDataSource.DB_TYPE == DbType.KING_BASE) {
                defaultValueTest = mapper.getById(11);
            } else {
                defaultValueTest = mapper.getById(1);
            }

            assertNotNull(defaultValueTest.getId());
            if (TestDataSource.DB_TYPE == DbType.ORACLE || TestDataSource.DB_TYPE == DbType.KING_BASE) {
                assertNull(defaultValueTest.getValue1());
            } else {
                assertNotNull(defaultValueTest.getValue1());
            }

            assertNotNull(defaultValueTest.getValue2());
            assertNotNull(defaultValueTest.getCreateTime());

            if (TestDataSource.DB_TYPE == DbType.ORACLE || TestDataSource.DB_TYPE == DbType.KING_BASE) {
                defaultValueTest2 = mapper.getById(12);
            } else {
                defaultValueTest2 = mapper.getById(2);
            }

            assertNotNull(defaultValueTest2.getId());
            if (TestDataSource.DB_TYPE == DbType.ORACLE || TestDataSource.DB_TYPE == DbType.KING_BASE) {
                assertNull(defaultValueTest2.getValue1());
            } else {
                assertNotNull(defaultValueTest2.getValue1());
            }
            assertNotNull(defaultValueTest2.getValue2());
            assertNotNull(defaultValueTest2.getCreateTime());
        }
    }

    @Test
    public void insertSelect() {
        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            DefaultValueTestMapper mapper = session.getMapper(DefaultValueTestMapper.class);
            DefaultValueTest defaultValueTest = new DefaultValueTest();
            //defaultValueTest.setId(1);
            defaultValueTest.setValue2(13);
            defaultValueTest.setValue3(TestEnum.X2);
            mapper.save(defaultValueTest);
            System.out.println(defaultValueTest);
            defaultValueTest = mapper.getById(1);
            System.out.println(defaultValueTest);
            if (TestDataSource.DB_TYPE != DbType.ORACLE && TestDataSource.DB_TYPE != DbType.KING_BASE) {
                InsertChain insert = InsertChain.of(mapper)
                        .insert(DefaultValueTest.class)
                        //.insertIgnore()
                        .field(DefaultValueTest::getValue1, DefaultValueTest::getValue2, DefaultValueTest::getValue3, DefaultValueTest::getCreateTime)
                        .fromSelect(Query
                                .create()
                                .select(DefaultValueTest::getValue1, DefaultValueTest::getValue2, DefaultValueTest::getValue3, DefaultValueTest::getCreateTime)
                                .from(DefaultValueTest.class)
                                .eq(DefaultValueTest::getId, 1)
                        );
                insert.execute();
                defaultValueTest = mapper.getById(2);
                assertEquals(defaultValueTest.getValue2(), 13);
                assertEquals(defaultValueTest.getValue3(), TestEnum.X2);
            }

        }
    }

//    @Test
//    public void testBatch() {
//        int length = 20000;
//        List<DefaultValueTest> list = new ArrayList<>(length);
//        for (int i = 0; i < length; i++) {
//            list.add(new DefaultValueTest());
//        }
//
//        long startTime = 0;
//        startTime = System.currentTimeMillis();
//        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
//            DefaultValueTestMapper mapper = session.getMapper(DefaultValueTestMapper.class);
//            for (DefaultValueTest item : list) {
//                mapper.save(item);
//            }
//        }
//        System.out.println("普通：" + (System.currentTimeMillis() - startTime));
//
//
//        startTime = System.currentTimeMillis();
//        int xx = MybatisBatchUtil.batchSave(this.sqlSessionFactory, DefaultValueTestMapper.class, list);
//        //System.out.println(xx);
//        System.out.println("批量：" + (System.currentTimeMillis() - startTime));
//
//
//        startTime = System.currentTimeMillis();
//        List<DefaultValueTest> saveBatchList = new ArrayList<>(100);
//        int saveTotalCnt = MybatisBatchUtil.batch(this.sqlSessionFactory, (session) -> {
//            DefaultValueTestMapper mapper = session.getMapper(DefaultValueTestMapper.class);
//            int saveCnt = 0;
//            int j=0;
//            for (int i = 0; i < length; i++) {
//                saveBatchList.add(list.get(i));
//                if (i != 0 && i % 100 == 0) {
//                    j++;
//                    mapper.saveBatch(saveBatchList, DefaultValueTest::getValue1, DefaultValueTest::getValue2, DefaultValueTest::getCreateTime);
//                    saveBatchList.clear();
//                }
//                if (i != 0 && j == 5) {
//                    j=0;
//                    saveCnt += MybatisBatchUtil.getEffectCnt(session.flushStatements());
//                }
//            }
//            if (!saveBatchList.isEmpty()) {
//                mapper.saveBatch(saveBatchList, DefaultValueTest::getValue1, DefaultValueTest::getValue2, DefaultValueTest::getCreateTime);
//                saveBatchList.clear();
//                saveCnt += MybatisBatchUtil.getEffectCnt(session.flushStatements());
//            }
//            assertEquals(length, saveCnt);
//            assertEquals(length, QueryChain.of(mapper).count());
//            return saveCnt;
//
//        });
//        //System.out.println(saveTotalCnt);
//
//        System.out.println("批量+原生批量：" + (System.currentTimeMillis() - startTime));
//
//    }
}
