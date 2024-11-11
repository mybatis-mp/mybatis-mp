package cn.mybatis.mp.core.mybatis.mapper;

import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.BaseDelete;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;
import java.util.Map;

public interface CRUDMapper {
    /**
     * 动态查询
     *
     * @param query 查询query
     * @param <T>   返回类
     * @return 返回单个对象
     */
    <T> T get(BaseQuery<? extends BaseQuery, T> query);

    /**
     * 是否存在
     *
     * @param query 子查询
     * @return 是否存在
     */
    boolean exists(BaseQuery<? extends BaseQuery, ?> query);

    /**
     * 动态插入
     *
     * @param insert
     * @return 影响条数
     */
    int save(BaseInsert<?> insert);

    /**
     * 动态修改
     *
     * @param update 修改update
     * @return 修改的条数
     */
    int update(BaseUpdate<?> update);

    /**
     * 动态删除
     *
     * @param delete 上下文
     * @return 删除条数
     */
    int delete(BaseDelete<?> delete);


    /**
     * 列表查询
     *
     * @param query 查询query
     * @return 返回查询列表
     */
    <T> List<T> list(BaseQuery<? extends BaseQuery, T> query);

    /**
     * 游标查询
     *
     * @param query 查询query
     * @return 返回游标
     */
    <T> Cursor<T> cursor(BaseQuery<? extends BaseQuery, T> query);

    /**
     * count查询
     *
     * @param query 上下文
     * @return 返回count 数
     */
    Integer count(BaseQuery<? extends BaseQuery, ?> query);


    /**
     * 分页查询
     *
     * @param query 查询query
     * @param pager 分页参数
     * @return 分页结果
     */
    <T, P extends Pager<T>> P paging(BaseQuery<? extends BaseQuery, T> query, P pager);

    /**
     * 将结果转成map
     *
     * @param mapKey 指定的map的key属性
     * @param query  查询对象
     * @param <K>    map的key
     * @param <V>    map的value
     * @return
     */
    <K, V> Map<K, V> mapWithKey(String mapKey, BaseQuery<? extends BaseQuery, V> query);
}
