package cn.mybatis.mp.core.sql.executor.chain;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.sql.executor.BaseInsert;

import java.util.Objects;

public class InsertChain extends BaseInsert<InsertChain> {

    protected MybatisMapper<?> mapper;

    protected InsertChain() {

    }

    public InsertChain(MybatisMapper<?> mapper) {
        this.mapper = mapper;
    }

    public static InsertChain of(MybatisMapper<?> mapper) {
        return new InsertChain(mapper);
    }

    /**
     * 非特殊情况 请使用of静态方法
     * 使用此方法后 execute 等执行方法 第一个参数必须是mapper
     *
     * @return
     */
    public static InsertChain create() {
        return new InsertChain();
    }

    private void setDefault() {
        if (this.getInsertTable() == null) {
            //自动设置实体类
            this.insert(mapper.getEntityType());
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
     * 执行
     *
     * @return
     */
    public int execute() {
        this.setDefault();
        return mapper.save(this);
    }

    /**
     * 执行
     *
     * @param mapper 操作目标实体类的mapper
     * @return
     */
    public <T> int execute(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this.execute();
    }
}
