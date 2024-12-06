/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package cn.mybatis.mp.core.mybatis.mapper;


import cn.mybatis.mp.core.function.ThreeFunction;
import cn.mybatis.mp.core.mybatis.mapper.context.*;
import cn.mybatis.mp.core.mybatis.mapper.mappers.basicMapper.*;
import cn.mybatis.mp.core.mybatis.provider.TablePrefixUtil;
import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.executor.SelectorCall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@Mapper
public interface BasicMapper extends BaseMapper, GetBasicMapper, ExistsBasicMapper, CountBasicMapper, ListBasicMapper, CursorBasicMapper,
        PagingBasicMapper, MapWithKeyBasicMapper, SaveBasicMapper, SaveOrUpdateBasicMapper, SaveModelBasicMapper, SaveOrUpdateModelBasicMapper,
        UpdateBasicMapper, UpdateModelBasicMapper, DeleteBasicMapper {

    /**
     * 获取当前数据库的类型
     *
     * @return DbType
     */
    DbType getCurrentDbType();

    /**
     * 选择器 不同数据库执行不同的方法
     *
     * @param consumer
     */
    <R> R dbAdapt(Consumer<SelectorCall<R>> consumer);

    /**
     * 获取基础Mapper
     *
     * @return BasicMapper
     */
    default BasicMapper getBasicMapper() {
        return this;
    }

    /**
     * 获取SqlSession 执行底层的 方法
     * @param function 提供SqlSession，返回
     * @return R
     */
    <R> R withSqlSession(Function<SqlSession, R> function);

    /**
     * 获取SqlSession 执行底层的 方法
     *
     * @param statement mybatis的statement ID，假如是 .开头，会自动帮你拼上cn.mybatis.mp.core.mybatis.mapper.BasicMapper
     * @param function  提供SqlSession，返回
     * @return R
     */
    <R> R withSqlSession(String statement, BiFunction<String, SqlSession, R> function);

    /**
     * 获取SqlSession 执行底层的 方法
     *
     * @param statement mybatis的statement ID，假如是 .开头，会自动帮你拼上cn.mybatis.mp.core.mybatis.mapper.BasicMapper
     * @param params    参数 可POJO 可Map 可其他
     * @param function  提供statement,params,SqlSession，返回你需要返回的信息；这里params 可能会被框架修改例如 where 对象
     * @return R
     */
    <R,PARAMS> R withSqlSession(String statement, PARAMS params, ThreeFunction<String, PARAMS, SqlSession, R> function);

    @Override
    default <T> T get(BaseQuery<? extends BaseQuery, T> query) {
        return this.$get(new SQLCmdQueryContext(query), new RowBounds(0, 2));
    }

    @Override
    default boolean exists(BaseQuery<? extends BaseQuery, ?> query) {
        if (Objects.isNull(query.getSelect())) {
            query.select1();
        }
        query.dbAdapt((q, selector) -> {
            selector.when(DbType.SQL_SERVER, () -> {
                query.getSelect().top(1);
                query.removeLimit();
            }).otherwise(() -> {
                query.limit(1);
            });
        });

        query.setReturnType(Integer.class);
        Integer obj = (Integer) this.get(query);
        return Objects.nonNull(obj) && obj >= 1;
    }

    @Override
    default int save(BaseInsert<?> insert) {
        return this.$save(new SQLCmdInsertContext<>(insert));
    }

    @Override
    default int update(BaseUpdate<?> update) {
        return this.$update(new SQLCmdUpdateContext(update));
    }

    @Override
    default int delete(BaseDelete<?> delete) {
        return this.$delete(new SQLCmdDeleteContext(delete));
    }

    @Override
    default <T> List<T> list(BaseQuery<? extends BaseQuery, T> query) {
        return this.$list(new SQLCmdQueryContext(query));
    }

    @Override
    default <T> Cursor<T> cursor(BaseQuery<? extends BaseQuery, T> query) {
        return this.$cursor(new SQLCmdQueryContext(query));
    }

    @Override
    default Integer count(BaseQuery<? extends BaseQuery, ?> query) {
        query.setReturnType(Integer.class);
        return this.$count(new SQLCmdCountQueryContext(query));
    }

    @Override
    default <T, P extends Pager<T>> P paging(BaseQuery<? extends BaseQuery, T> query, P pager) {
        if (pager.isExecuteCount()) {
            Class returnType = query.getReturnType();
            TablePrefixUtil.prefixMapping(query, returnType);
            query.setReturnType(Integer.class);
            Integer count = this.$countFromQuery(new SQLCmdCountFromQueryContext(query));
            query.setReturnType(returnType);

            pager.setTotal(Optional.of(count).orElse(0));
            if (pager.getTotal() < 1) {
                pager.setResults(Collections.emptyList());
                return pager;
            }
        }
        query.limit(pager.getOffset(), pager.getSize());
        pager.setResults(this.list(query));
        return pager;
    }

    @Override
    default <K, V> Map<K, V> mapWithKey(String mapKey, BaseQuery<? extends BaseQuery, V> query) {
        return this.$mapWithKey(new MapKeySQLCmdQueryContext(mapKey, query));
    }
}
