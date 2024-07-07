package cn.mybatis.mp.core.sql.executor;

import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.context.MybatisParameter;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.Methods;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.cmd.executor.AbstractInsert;
import db.sql.api.impl.tookit.LambdaUtil;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.util.*;

public abstract class BaseInsert<T extends BaseInsert<T>> extends AbstractInsert<T, MybatisCmdFactory> {
    private Getter<?>[] fields;
    private Map<Class<?>, TableInfo> tableInfoMap = new HashMap<>();

    public BaseInsert() {
        super(new MybatisCmdFactory());
    }

    @Override
    @SafeVarargs
    public final <T2> T field(Getter<T2>... fields) {
        this.fields = fields;
        return super.field(fields);
    }

    @Override
    public T values(List<Object> values) {
        if (Objects.isNull(this.fields)) {
            return super.values(values);
        }
        List<Cmd> cmdValues = new ArrayList<>(values.size());
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if (value instanceof Cmd || value instanceof MybatisParameter) {
                cmdValues.add(Methods.convert(value));
                continue;
            }
            LambdaUtil.LambdaFieldInfo fieldInfo = LambdaUtil.getFieldInfo(this.fields[i]);
            TableInfo tableInfo = tableInfoMap.computeIfAbsent(fieldInfo.getType(), key -> {
                return Tables.get(key);
            });

            TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(fieldInfo.getName());
            if (tableFieldInfo.getField().getType().isAssignableFrom(value.getClass()) && tableFieldInfo.getTableFieldAnnotation().typeHandler() != UnknownTypeHandler.class) {
                value = MybatisParameter.create(value, tableFieldInfo.getTableFieldAnnotation().typeHandler(), tableFieldInfo.getTableFieldAnnotation().jdbcType());
            }
            cmdValues.add(Methods.convert(value));
        }
        this.$values(cmdValues);
        return (T) this;
    }

    /**************以下为去除警告************/

    @Override
    @SafeVarargs
    public final T field(TableField... fields) {
        return super.field(fields);
    }

    /**************以上为去除警告************/
}
