package cn.mybatis.mp.generator.config;

import cn.mybatis.mp.generator.strategy.DefaultValueConvert;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * 列配置
 */
@Getter
public class ColumnConfig {

    /**
     * 排除列
     */
    private final List<String> excludeColumns = new ArrayList<>();

    /**
     * 禁止修改的列
     */
    private final List<String> disableUpdateColumns = new ArrayList<>();

    /**
     * 禁止查询列
     */
    private final List<String> disableSelectColumns = new ArrayList<>();

    /**
     * 乐观锁列名
     */
    private String versionColumn = "";

    /**
     * 租户ID列名
     */
    private String tenantIdColumn = "";

    /**
     * 逻辑删除列名
     */
    private String logicDeleteColumn = "";

    /**
     * 默认值转换
     */
    private Function<String, String> defaultValueConvert = new DefaultValueConvert()::convert;

    /**
     * 排除列
     */
    public ColumnConfig excludeColumns(String... columns) {
        this.excludeColumns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * 禁止修改的列
     */
    public ColumnConfig disableUpdateColumns(String... columns) {
        this.disableUpdateColumns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * 禁止查询列
     */
    public ColumnConfig disableSelectColumns(String... columns) {
        this.disableSelectColumns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * 乐观锁列名
     *
     * @param versionColumn
     * @return
     */
    public ColumnConfig versionColumn(String versionColumn) {
        this.versionColumn = versionColumn;
        return this;
    }

    /**
     * 租户ID列名
     *
     * @param tenantIdColumn
     * @return
     */
    public ColumnConfig tenantIdColumn(String tenantIdColumn) {
        this.tenantIdColumn = tenantIdColumn;
        return this;
    }

    /**
     * 逻辑列名
     *
     * @param logicDeleteColumn
     * @return
     */
    public ColumnConfig logicDeleteColumn(String logicDeleteColumn) {
        this.logicDeleteColumn = logicDeleteColumn;
        return this;
    }

    /**
     * 默认值转换
     *
     * @param defaultValueConvert
     * @return
     */
    public ColumnConfig defaultValueConvert(Function<String, String> defaultValueConvert) {
        this.defaultValueConvert = defaultValueConvert;
        return this;
    }
}
