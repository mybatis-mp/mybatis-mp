package cn.mybatis.mp.core.sql.executor.chain;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.Query;
import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.GetterField;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.tookit.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class InsertSelectChain<T> extends BaseInsert<InsertSelectChain<T>> {

    private final Map<Getter<T>, Object> insertSelectFields = new HashMap<>();
    protected MybatisMapper<?> mapper;

    protected InsertSelectChain() {

    }

    public InsertSelectChain(MybatisMapper<T> mapper) {
        this.mapper = mapper;
    }

    public static <T> InsertSelectChain<T> of(MybatisMapper<T> mapper) {
        return new InsertSelectChain(mapper);
    }

    /**
     * 非特殊情况 请使用of静态方法
     * 使用此方法后 后续执行查询需调用一次withMapper(mybatisMapper)方法
     *
     * @return 自己
     */
    public static <T> InsertSelectChain<T> create() {
        return new InsertSelectChain<>();
    }

    public <T2> InsertSelectChain<T> insertSelect(Getter<T> field, Cmd select) {
        this.insertSelectFields.put(field, select);
        return this;
    }

    public <T2> InsertSelectChain<T> insertSelect(Getter<T> field, Getter<T2> select) {
        this.insertSelectFields.put(field, select);
        return this;
    }

    public <T2> InsertSelectChain<T> insertSelect(Getter<T> field, Getter<T2> select, Function<TableField, Cmd> fun) {
        this.insertSelectFields.put(field, new SelectGetterFun<>(select, fun));
        return this;
    }

    public InsertSelectChain<T> insertSelect(Getter<T> field, GetterField[] select, Function<TableField[], Cmd> fun) {
        this.insertSelectFields.put(field, new SelectGetterFieldsFun(select, fun));
        return this;
    }

    public InsertSelectChain<T> insertSelectQuery(Consumer<Query<?>> consumer) {
        if (!insertSelectFields.isEmpty()) {
            List<Getter<T>> fields = new ArrayList<>();
            Query<?> selectQuery = Query.create();
            for (Map.Entry<Getter<T>, Object> entry : insertSelectFields.entrySet()) {
                fields.add(entry.getKey());
                if (entry.getValue() instanceof InsertSelectChain.SelectGetterFun) {
                    SelectGetterFun<?> selectGetterFun = (SelectGetterFun<?>) entry.getValue();
                    selectQuery.select(selectGetterFun.field, selectGetterFun.fun);
                } else if (entry.getValue() instanceof SelectGetterFieldsFun) {
                    SelectGetterFieldsFun selectGetterFieldsFun = (SelectGetterFieldsFun) entry.getValue();
                    selectQuery.select(selectGetterFieldsFun.fields, selectGetterFieldsFun.fun);
                } else if (entry.getValue() instanceof Cmd) {
                    selectQuery.select((Cmd) entry.getValue());
                } else {
                    selectQuery.select((Getter<?>) entry.getValue());
                }
            }
            this.field(fields.toArray(new Getter[fields.size()]));
            this.fromSelect(selectQuery);
            insertSelectFields.clear();
            if (Objects.nonNull(consumer)) {
                consumer.accept(selectQuery);
            }
        }
        return this;
    }

    private void setDefault() {
        if (this.getInsertTable() == null) {
            //自动设置实体类
            this.insert(mapper.getEntityType());
        }
    }

    private void checkAndSetMapper(MybatisMapper mapper) {
        if (java.util.Objects.isNull(this.mapper)) {
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
    public InsertSelectChain<T> withMapper(MybatisMapper<T> mapper) {
        this.checkAndSetMapper(mapper);
        return this;
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

    private static class SelectGetterFun<T> {

        public Getter<T> field;

        public Function<TableField, Cmd> fun;

        public SelectGetterFun(Getter<T> field, Function<TableField, Cmd> fun) {
            this.field = field;
            this.fun = fun;
        }
    }

    private static class SelectGetterFieldsFun {

        public GetterField[] fields;

        public Function<TableField[], Cmd> fun;

        public SelectGetterFieldsFun(GetterField[] fields, Function<TableField[], Cmd> fun) {
            this.fields = fields;
            this.fun = fun;
        }
    }
}
