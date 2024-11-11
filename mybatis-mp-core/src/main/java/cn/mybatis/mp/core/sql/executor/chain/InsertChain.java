package cn.mybatis.mp.core.sql.executor.chain;

import cn.mybatis.mp.core.mybatis.mapper.MybatisMapper;
import cn.mybatis.mp.core.sql.executor.BaseInsert;
import cn.mybatis.mp.core.sql.executor.Query;
import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.SqlBuilderContext;
import db.sql.api.cmd.GetterField;
import db.sql.api.cmd.executor.IQuery;
import db.sql.api.impl.cmd.basic.TableField;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class InsertChain extends BaseInsert<InsertChain> {

    private final Map<Getter<?>, Object> insertSelectFields = new HashMap<>();

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
     * 使用此方法后 后续执行查询需调用一次withMapper(mybatisMapper)方法
     *
     * @return 自己
     */
    public static InsertChain create() {
        return new InsertChain();
    }

    public <T> InsertChain insertSelect(Getter<T> field, Cmd select) {
        this.insertSelectFields.put(field, select);
        return this;
    }

    public <T, T2> InsertChain insertSelect(Getter<T> field, Getter<T2> select) {
        this.insertSelectFields.put(field, select);
        return this;
    }

    public <T, T2> InsertChain insertSelect(Getter<T> field, Getter<T2> select, Function<TableField, Cmd> fun) {
        this.insertSelectFields.put(field, new SelectGetterFun<>(select, fun));
        return this;
    }

    public <T> InsertChain insertSelect(Getter<T> field, GetterField[] select, Function<TableField[], Cmd> fun) {
        this.insertSelectFields.put(field, new SelectGetterFieldsFun(select, fun));
        return this;
    }

    private void buildSelectQuery() {
        if (!insertSelectFields.isEmpty()) {
            List<Getter<?>> fields = new ArrayList<>();
            IQuery selectQuery = this.getInsertSelect().getSelectQuery();
            for (Map.Entry<Getter<?>, Object> entry : insertSelectFields.entrySet()) {
                fields.add(entry.getKey());
                if (entry.getValue() instanceof SelectGetterFun) {
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
            this.fields(fields.toArray(new Getter[0]));
            this.fromSelect(selectQuery);
            insertSelectFields.clear();
        }
    }

    public InsertChain insertSelectQuery(Consumer<Query<?>> consumer) {
        if (!insertSelectFields.isEmpty()) {
            Query<?> selectQuery = Query.create();
            this.fromSelect(selectQuery);
            if (db.sql.api.impl.tookit.Objects.nonNull(consumer)) {
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
    public <T> InsertChain withMapper(MybatisMapper<T> mapper) {
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

    @Override
    public StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        this.selectorExecute(context.getDbType());
        this.buildSelectQuery();
        return super.sql(module, parent, context, sqlBuilder);
    }

    @Override
    public StringBuilder sql(SqlBuilderContext context, StringBuilder sqlBuilder) {
        this.selectorExecute(context.getDbType());
        this.buildSelectQuery();
        return super.sql(context, sqlBuilder);
    }

    private static class SelectGetterFun<T> {

        public final Getter<T> field;

        public final Function<TableField, Cmd> fun;

        public SelectGetterFun(Getter<T> field, Function<TableField, Cmd> fun) {
            this.field = field;
            this.fun = fun;
        }
    }

    private static class SelectGetterFieldsFun {

        public final GetterField[] fields;

        public final Function<TableField[], Cmd> fun;

        public SelectGetterFieldsFun(GetterField[] fields, Function<TableField[], Cmd> fun) {
            this.fields = fields;
            this.fun = fun;
        }
    }
}
