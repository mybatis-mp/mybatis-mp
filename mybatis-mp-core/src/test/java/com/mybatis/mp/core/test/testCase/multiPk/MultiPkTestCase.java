package com.mybatis.mp.core.test.testCase.multiPk;

import com.mybatis.mp.core.test.DO.MultiPk;
import com.mybatis.mp.core.test.mapper.MultiPkMapper;
import com.mybatis.mp.core.test.testCase.BaseTest;
import org.apache.ibatis.session.SqlSession;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiPkTestCase extends BaseTest {

    private MultiPk getById(MultiPkMapper mapper, Integer id1, Integer id2) {
        return mapper.get(where -> {
            where.eq(MultiPk::getId1, id1).eq(MultiPk::getId2, id2);
        });
    }

    @Test
    public void oneTest() {

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper mapper = session.getMapper(MultiPkMapper.class);
            MultiPk entity = new MultiPk();
            entity.setId1(1);
            entity.setId2(2);
            entity.setName("12");
            mapper.save(entity);

            assertEquals(entity, getById(mapper, 1, 2));
            entity.setName("12update");
            mapper.update(entity);
            mapper.saveOrUpdate(entity);

            assertEquals("12update", getById(mapper, 1, 2).getName());
            mapper.delete(entity);

            assertEquals(null, getById(mapper, 1, 2));
        }

    }

    @Test
    public void multiTest() {

        try (SqlSession session = this.sqlSessionFactory.openSession(false)) {
            MultiPkMapper mapper = session.getMapper(MultiPkMapper.class);
            MultiPk entity = new MultiPk();
            entity.setId1(1);
            entity.setId2(2);
            entity.setName("12");
            mapper.saveOrUpdate(entity);

            assertEquals(entity, getById(mapper, 1, 2));
            entity.setName("12update");

            List<MultiPk> list = Arrays.asList(entity);
            mapper.update(list);

            assertEquals("12update", getById(mapper, 1, 2).getName());

            mapper.delete(list);
            assertEquals(null, getById(mapper, 1, 2));
        }

    }
}
