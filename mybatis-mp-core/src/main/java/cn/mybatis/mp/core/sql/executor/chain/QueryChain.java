package cn.mybatis.mp.core.sql.executor.chain;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 查询链路
 */
public class QueryChain extends BaseQuery<QueryChain> {

    protected final MybatisMapper<?> mapper;

    public QueryChain(MybatisMapper<?> mapper) {
        this.mapper = mapper;
    }

    public QueryChain(MybatisMapper<?> mapper, Where where) {
        super(where);
        this.mapper = mapper;
    }

    public static QueryChain of(MybatisMapper<?> mapper) {
        return new QueryChain(mapper);
    }

    public static QueryChain of(MybatisMapper<?> mapper, Where where) {
        return new QueryChain(mapper, where);
    }

    private void setDefault() {
        this.setDefault(false);
    }

    private void setDefault(boolean forCount) {
        if (Objects.isNull(this.select)) {
            if (forCount) {
                this.selectCountAll();
            } else {
                this.select(mapper.getEntityType());
            }
        }
        if (Objects.isNull(this.from)) {
            this.from(mapper.getEntityType());
        }
        if (Objects.isNull(this.returnType)) {
            this.returnType(mapper.getEntityType());
        }
    }

    /**
     * 获取单个对象
     *
     * @param <R>
     * @return
     */
    public <R> R get() {
        return this.get(true);
    }

    /**
     * 获取单个对象
     *
     * @param optimize 是否自动优化
     * @param <R>
     * @return
     */
    public <R> R get(boolean optimize) {
        this.setDefault(false);
        return (R) mapper.get(this, optimize);
    }

    /**
     * 获取列表
     *
     * @param <R>
     * @return
     */
    public <R> List<R> list() {
        return this.list(true);
    }

    /**
     * 获取列表
     *
     * @param optimize 是否自动优化
     * @param <R>
     * @return
     */
    public <R> List<R> list(boolean optimize) {
        this.setDefault(false);
        return mapper.list(this, optimize);
    }

    /**
     * 获取列表
     *
     * @param <R>
     * @return
     */
    public <R> Cursor<R> cursor() {
        return this.cursor(true);
    }

    /**
     * 获取列表
     *
     * @param optimize 是否自动优化
     * @param <R>
     * @return
     */
    public <R> Cursor<R> cursor(boolean optimize) {
        this.setDefault(false);
        return mapper.cursor(this, optimize);
    }


    /**
     * 获取条数
     *
     * @return
     */
    public Integer count() {
        if (this.select == null) {
            this.selectCountAll();
        }
        this.setDefault(true);
        return mapper.count(this);
    }

    /**
     * 判断是否存在
     *
     * @return
     */
    public boolean exists() {
        return this.exists(true);
    }

    /**
     * 判断是否存在
     *
     * @param optimize 是否自动优化
     * @return
     */
    public boolean exists(boolean optimize) {
        if (this.select == null) {
            this.select1();
        }
        this.limit(1);
        this.setDefault();
        return mapper.exists(this, optimize);
    }

    /**
     * 分页查询
     *
     * @param pager
     * @param <R>
     * @return
     */
    public <R, P extends Pager<R>> P paging(P pager) {
        this.setDefault();
        return mapper.paging(this, pager);
    }

    /**
     * 将结果转成map
     *
     * @param mapKey 指定的map的key属性
     * @param <K>    map的key
     * @param <V>    map的value
     * @return
     */
    public <K, V, T> Map<K, V> mapWithKey(Getter<T> mapKey) {
        return this.mapWithKey(mapKey, true);
    }

    /**
     * 将结果转成map
     *
     * @param mapKey   指定的map的key属性
     * @param optimize 是否优化sql
     * @param <K>      map的key
     * @param <V>      map的value
     * @return
     */
    public <K, V, T> Map<K, V> mapWithKey(Getter<T> mapKey, boolean optimize) {
        this.setDefault();
        return mapper.mapWithKey(mapKey, this, optimize);
    }
}
