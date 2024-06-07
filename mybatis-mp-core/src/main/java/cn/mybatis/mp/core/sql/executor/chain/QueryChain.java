package cn.mybatis.mp.core.sql.executor.chain;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import db.sql.api.GetterFun;
import db.sql.api.impl.cmd.struct.Where;
import org.apache.ibatis.cursor.Cursor;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 查询链路
 */
public class QueryChain<E> extends BaseQuery<QueryChain<E>, E> {

    protected MybatisMapper<E> mapper;

    protected QueryChain() {

    }

    public QueryChain(MybatisMapper<E> mapper) {
        this.mapper = mapper;
    }

    public QueryChain(MybatisMapper<E> mapper, Where where) {
        super(where);
        this.mapper = mapper;
    }

    /**
     * 非特殊情况 请使用of静态方法
     * 使用此方法后 get list paging count cursor exists mapWithKey等执行方法 第一个参数必须是mapper
     *
     * @param <E>
     * @return
     */
    public static <E> QueryChain<E> create() {
        return new QueryChain<>();
    }

    public static <E> QueryChain<E> of(MybatisMapper<E> mapper) {
        return new QueryChain<>(mapper);
    }

    public static <E> QueryChain<E> of(MybatisMapper<E> mapper, Where where) {
        return new QueryChain<>(mapper, where);
    }

    public <E2> QueryChain<E2> returnType(Class<E2> returnType) {
        return (QueryChain<E2>) super.setReturnType(returnType);
    }

    public <V> QueryChain<Map<String, V>> returnMap(Class<V> vClass) {
        return (QueryChain<Map<String, V>>) super.setReturnType(returnType);
    }

    public <V> QueryChain<Map<String, V>> returnMap() {
        return (QueryChain) super.setReturnType(Map.class);
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

    private void checkAndSetMapper(MybatisMapper mapper) {
        if (Objects.isNull(this.mapper)) {
            this.mapper = mapper;
            return;
        }
        if (this.mapper == mapper) {
            return;
        }
        throw new RuntimeException(" the mapper is already set, can't use another mapper");
    }

    /**
     * 获取单个对象
     *
     * @return
     */
    public E get() {
        return this.get(true);
    }

    /**
     * 获取单个对象
     *
     * @param optimize 是否自动优化
     * @return
     */
    public E get(boolean optimize) {
        this.setDefault(false);
        return mapper.get(this, optimize);
    }

    /**
     * 获取单个对象
     *
     * @param mapper 操作目标实体类的mapper
     * @return
     */
    public <T> E get(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this.get();
    }

    /**
     * 获取单个对象
     *
     * @param mapper   操作目标实体类的mapper
     * @param optimize 是否自动优化
     * @return
     */
    public <T> E get(MybatisMapper<T> mapper, boolean optimize) {
        this.checkAndSetMapper(mapper);
        return this.get(optimize);
    }

    /**
     * 获取列表
     *
     * @return
     */
    public List<E> list() {
        return this.list(true);
    }

    /**
     * 获取列表
     *
     * @param optimize 是否自动优化
     * @return
     */
    public List<E> list(boolean optimize) {
        this.setDefault(false);
        return mapper.list(this, optimize);
    }


    /**
     * 获取列表
     *
     * @param mapper 操作目标实体类的mapper
     * @return
     */
    public <T> List<E> list(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this.list();
    }

    /**
     * 获取列表
     *
     * @param mapper   操作目标实体类的mapper
     * @param optimize 是否自动优化
     * @return
     */
    public <T> List<E> list(MybatisMapper<T> mapper, boolean optimize) {
        this.checkAndSetMapper(mapper);
        return this.list(optimize);
    }

    /**
     * 获取列表
     *
     * @return
     */
    public Cursor<E> cursor() {
        return this.cursor(true);
    }

    /**
     * 获取列表
     *
     * @param mapper 操作目标实体类的mapper
     * @return
     */
    public <T> Cursor<E> cursor(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this.cursor();
    }

    /**
     * 获取列表
     *
     * @param optimize 是否自动优化
     * @return
     */
    public Cursor<E> cursor(boolean optimize) {
        this.setDefault(false);
        return mapper.cursor(this, optimize);
    }


    /**
     * 获取列表
     *
     * @param mapper   操作目标实体类的mapper
     * @param optimize 是否自动优化
     * @return
     */
    public <T> Cursor<E> cursor(MybatisMapper<T> mapper, boolean optimize) {
        this.checkAndSetMapper(mapper);
        return this.cursor(optimize);
    }


    /**
     * 获取条数
     *
     * @return
     */
    public Integer count() {
        return this.count(true);
    }

    /**
     * 获取条数
     *
     * @param optimize 是否自动优化
     * @return
     */
    public Integer count(boolean optimize) {
        if (this.select == null) {
            this.selectCountAll();
        }
        this.setDefault(true);
        return mapper.count(this, optimize);
    }

    /**
     * 获取条数
     *
     * @param mapper 操作目标实体类的mapper
     * @return
     */
    public <T> Integer count(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this.count();
    }

    /**
     * 获取条数
     *
     * @param mapper   操作目标实体类的mapper
     * @param optimize 是否自动优化
     * @return
     */
    public <T> Integer count(MybatisMapper<T> mapper, boolean optimize) {
        this.checkAndSetMapper(mapper);
        return this.count(optimize);
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
     * @param mapper 操作目标实体类的mapper
     * @return
     */
    public <T> boolean exists(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this.exists();
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
     * 判断是否存在
     *
     * @param mapper   操作目标实体类的mapper
     * @param optimize 是否自动优化
     * @return
     */
    public <T> boolean exists(MybatisMapper<T> mapper, boolean optimize) {
        this.checkAndSetMapper(mapper);
        return this.exists(optimize);
    }

    /**
     * 分页查询
     *
     * @param pager
     * @return
     */
    public <P extends Pager<E>> P paging(P pager) {
        this.setDefault();
        return mapper.paging(this, pager);
    }

    /**
     * 分页查询
     *
     * @param mapper 操作目标实体类的mapper
     * @param pager
     * @return
     */
    public <P extends Pager<E>, T> P paging(MybatisMapper<T> mapper, P pager) {
        this.checkAndSetMapper(mapper);
        return this.paging(pager);
    }

    /**
     * 将结果转成map
     *
     * @param mapKey 指定的map的key属性
     * @param <K>    map的key
     * @return
     */
    public <K> Map<K, E> mapWithKey(GetterFun<E, K> mapKey) {
        return this.mapWithKey(mapKey, true);
    }

    /**
     * 将结果转成map
     *
     * @param mapper 操作目标实体类的mapper
     * @param mapKey 指定的map的key属性
     * @param <K>    map的key
     * @return
     */
    public <K, T> Map<K, E> mapWithKey(MybatisMapper<T> mapper, GetterFun<E, K> mapKey) {
        this.checkAndSetMapper(mapper);
        return this.mapWithKey(mapKey);
    }

    /**
     * 将结果转成map
     *
     * @param mapKey   指定的map的key属性
     * @param optimize 是否优化sql
     * @param <K>      map的key
     * @return
     */
    public <K> Map<K, E> mapWithKey(GetterFun<E, K> mapKey, boolean optimize) {
        this.setDefault();
        return mapper.mapWithKey(mapKey, this, optimize);
    }

    /**
     * 将结果转成map
     *
     * @param mapper   操作目标实体类的mapper
     * @param mapKey   指定的map的key属性
     * @param optimize 是否优化sql
     * @param <K>      map的key
     * @return
     */
    public <K, T> Map<K, E> mapWithKey(MybatisMapper<T> mapper, GetterFun<E, K> mapKey, boolean optimize) {
        this.checkAndSetMapper(mapper);
        return this.mapWithKey(mapKey, optimize);
    }
}
