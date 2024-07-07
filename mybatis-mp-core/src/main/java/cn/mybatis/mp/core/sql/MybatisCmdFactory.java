package cn.mybatis.mp.core.sql;

import cn.mybatis.mp.core.MybatisMpConfig;
import cn.mybatis.mp.core.NotTableClassException;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.mybatis.mapper.context.MybatisLikeQueryParameter;
import cn.mybatis.mp.core.mybatis.mapper.context.MybatisQueryParameter;
import cn.mybatis.mp.core.mybatis.typeHandler.LikeQuerySupport;
import cn.mybatis.mp.core.mybatis.typeHandler.QuerySupport;
import cn.mybatis.mp.core.sql.executor.MpTable;
import cn.mybatis.mp.core.util.TableInfoUtil;
import db.sql.api.Cmd;
import db.sql.api.Getter;
import db.sql.api.cmd.LikeMode;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.basic.Table;
import db.sql.api.impl.cmd.basic.TableField;
import db.sql.api.impl.tookit.LambdaUtil;
import org.apache.ibatis.util.MapUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * CMD 命令工厂
 * 增加了对实体类的映射
 */
public class MybatisCmdFactory extends CmdFactory {

    private Map<Class<?>, TableInfo> tableInfoMap = new HashMap<>();

    public MybatisCmdFactory() {
        super();
    }

    public MybatisCmdFactory(String tableAsPrefix) {
        super(tableAsPrefix);
    }

    @Override
    public Table table(Class entity, int storey) {
        return MapUtil.computeIfAbsent(this.tableCache, String.format("%s.%s", entity.getName(), storey), key -> {
            TableInfo tableInfo = Tables.get(entity);
            tableNums++;
            Table table = new MpTable(tableInfo.getSchemaAndTableName());
            table.as(tableAs(storey, tableNums));
            return table;
        });
    }

    @Override
    public <T> TableField field(Getter<T> column, int storey) {
        LambdaUtil.LambdaFieldInfo fieldInfo = LambdaUtil.getFieldInfo(column);
        return this.field(fieldInfo.getType(), fieldInfo.getName(), storey);
    }

    @Override
    public <T> String columnName(Getter<T> column) {
        return TableInfoUtil.getColumnName(column);
    }

    @Override
    public TableField field(Class entity, String filedName, int storey) {
        TableInfo tableInfo;
        try {
            tableInfo = Tables.get(entity);
        } catch (NotTableClassException e) {
            throw new RuntimeException(String.format("class %s is not entity", entity.getName()));
        }

        TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(filedName);
        if (Objects.isNull(tableFieldInfo)) {
            throw new RuntimeException(String.format("property %s is not a column", filedName));
        }
        Table table = table(entity, storey);
        return new TableField(table, tableFieldInfo.getColumnName());
    }

    @Override
    public <T> TableField[] fields(int storey, Getter<T>... columns) {
        if (columns.length < 2) {
            return new TableField[]{this.field(columns[0], 1)};
        }
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(columns[0]);
        TableInfo tableInfo = Tables.get(lambdaFieldInfo.getType());
        Table table = this.table(lambdaFieldInfo.getType(), storey);
        TableField[] tableFields = new TableField[columns.length];
        for (int i = 0; i < columns.length; i++) {
            tableFields[i] = table.$(tableInfo.getFieldInfo(LambdaUtil.getName(columns[i])).getColumnName());
        }
        return tableFields;
    }

    @Override
    public boolean isEnableConditionParamWrap() {
        return MybatisMpConfig.getConditionParamTypeHandlerWrap();
    }

    @Override
    public <T> Object conditionParamWrap(Getter<T> column, Object param) {
        if (!isEnableConditionParamWrap()) {
            return param;
        }
        if (Objects.isNull(param) || param instanceof Cmd) {
            return param;
        }
        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(column);
        TableInfo tableInfo = tableInfoMap.computeIfAbsent(lambdaFieldInfo.getType(), key -> {
            return Tables.get(key);
        });

        TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(lambdaFieldInfo.getName());
        if (tableFieldInfo.getField().getType().isAssignableFrom(param.getClass())) {
            Class typeHandler = tableFieldInfo.getTableFieldAnnotation().typeHandler();
            if (QuerySupport.class.isAssignableFrom(typeHandler)) {
                return new MybatisQueryParameter(param, typeHandler, tableFieldInfo.getTableFieldAnnotation().jdbcType());
            }
        }
        return param;
    }

    @Override
    public <T> Object[] likeParamWrap(Getter<T> column, Object param, LikeMode likeMode, boolean isNotLike) {
        if (!isEnableConditionParamWrap()) {
            return new Object[]{likeMode, param};
        }
        if (Objects.isNull(param) || param instanceof Cmd) {
            return new Object[]{likeMode, param};
        }

        LambdaUtil.LambdaFieldInfo lambdaFieldInfo = LambdaUtil.getFieldInfo(column);
        TableInfo tableInfo = tableInfoMap.computeIfAbsent(lambdaFieldInfo.getType(), key -> {
            return Tables.get(key);
        });

        TableFieldInfo tableFieldInfo = tableInfo.getFieldInfo(lambdaFieldInfo.getName());
        if (tableFieldInfo.getField().getType().isAssignableFrom(param.getClass())) {
            Class typeHandler = tableFieldInfo.getTableFieldAnnotation().typeHandler();
            if (LikeQuerySupport.class.isAssignableFrom(typeHandler)) {
                LikeQuerySupport likeQuerySupport = (LikeQuerySupport) tableFieldInfo.getTypeHandler();
                param = new MybatisLikeQueryParameter(param, isNotLike, likeMode, typeHandler, tableFieldInfo.getTableFieldAnnotation().jdbcType());
                likeMode = likeQuerySupport.convertLikeMode(likeMode, isNotLike);
            } else if (QuerySupport.class.isAssignableFrom(typeHandler)) {
                param = new MybatisQueryParameter(param, typeHandler, tableFieldInfo.getTableFieldAnnotation().jdbcType());
            }
        }
        return new Object[]{likeMode, param};
    }
}
