package cn.mybatis.mp.core.sql.executor.chain;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.sql.executor.BaseUpdate;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Objects;

/**
 * 更新链路
 */
public class UpdateChain extends BaseUpdate<UpdateChain> {

    protected MybatisMapper<?> mapper;

    protected UpdateChain() {

    }

    public UpdateChain(MybatisMapper<?> mapper) {
        this.mapper = mapper;
    }

    public UpdateChain(MybatisMapper<?> mapper, Where where) {
        super(where);
        this.mapper = mapper;
    }

    public static UpdateChain of(MybatisMapper<?> mapper) {
        return new UpdateChain(mapper);
    }

    public static UpdateChain of(MybatisMapper<?> mapper, Where where) {
        return new UpdateChain(mapper, where);
    }

    /**
     * 非特殊情况 请使用of静态方法
     * 使用此方法后 execute 等执行方法 第一个参数必须是mapper
     *
     * @return
     */
    public static UpdateChain create() {
        return new UpdateChain();
    }

    private void setDefault() {
        if (Objects.isNull(this.getUpdateTable())) {
            //自动设置实体类
            this.update(mapper.getEntityType());
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
        return mapper.update(this);
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
