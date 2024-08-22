package cn.mybatis.mp.core.sql.executor.chain;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.mybatis.mapper.context.Pager;
import cn.mybatis.mp.core.sql.executor.BaseQuery;
import cn.mybatis.mp.core.sql.util.SelectClassUtil;
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
     * 使用此方法后 后续执行查询需调用一次withMapper(mybatisMapper)方法
     *
     * @param <E>
     * @return 自己
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
                boolean hasSetSelect = false;
                if (Objects.nonNull(this.returnType)) {
                    hasSetSelect = SelectClassUtil.select(this, this.returnType);
                }
                if (!hasSetSelect) {
                    this.select(mapper.getEntityType());
                }
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
     * 用create静态方法的 Chain 需要调用一次此方法 用于设置 mapper
     *
     * @param mapper 操作目标实体类的mapper
     * @return 自己
     */
    public <T> QueryChain<E> withMapper(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this;
    }

    /**
     * 获取单个对象
     *
     * @return
     */
    public E get() {
        this.setDefault(false);
        return mapper.get(this);
    }

    /**
     * 获取列表
     *
     * @return
     */
    public List<E> list() {
        this.setDefault(false);
        return mapper.list(this);
    }

    /**
     * 获取列表
     *
     * @return
     */
    public Cursor<E> cursor() {
        this.setDefault(false);
        return mapper.cursor(this);
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
        if (this.select == null) {
            this.select1();
        }
        this.limit(1);
        this.setDefault();
        return mapper.exists(this);
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
     * 将结果转成map
     *
     * @param mapKey   指定的map的key属性
     * @param <K>      map的key
     * @return
     */
    public <K> Map<K, E> mapWithKey(GetterFun<E, K> mapKey) {
        this.setDefault();
        return mapper.mapWithKey(mapKey, this);
    }
}
