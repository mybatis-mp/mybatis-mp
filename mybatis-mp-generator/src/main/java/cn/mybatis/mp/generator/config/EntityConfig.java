package cn.mybatis.mp.generator.config;

import cn.mybatis.mp.generator.database.meta.ColumnInfo;
import cn.mybatis.mp.generator.strategy.NamingStrategy;
import lombok.Getter;
import org.apache.ibatis.type.JdbcType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public class EntityConfig {

    /**
     * 列名前缀 - 设置后 可忽略前缀
     */
    private final List<String> columnPrefixes = new ArrayList<>();

    /**
     * 排除列
     */
    private final List<String> excludeColumns = new ArrayList<>();

    /**
     * 数据库类型的java映射
     */
    private final Map<JdbcType, Class<?>> typeMapping = new HashMap<>();


    /**
     * 是否开启 swagger
     */
    private boolean swagger = false;

    /**
     * 是否开启 serial
     */
    private boolean serial = false;

    /**
     * 实体类父类
     */
    private String superClass;
    /**
     * 是否使用 lombok
     */
    private boolean lombok = true;

    /**
     * 是否使用 lombok @Builder
     */
    private boolean lombokBuilder = false;
    /**
     * 注解上是否加上schema
     */
    private boolean schema = false;
    /**
     * 默认值是否可用
     */
    private boolean defaultValueEnable = true;
    /**
     * 默认TableId代码，数据库非自增时生效
     */
    private String defaultTableIdCode;

    /**
     * 逻辑删除代码
     */
    private String logicDeleteCode;
    /**
     * 实体类包名
     */
    private String packageName = "DO";
    /**
     * 实体类名字转换器
     */
    private Function<String, String> nameConvert;
    /**
     * 字段名策略
     */
    private NamingStrategy fieldNamingStrategy = NamingStrategy.UNDERLINE_TO_CAMEL;
    /**
     * 字段名转换器
     */
    private Function<String, String> fieldNameConverter;
    /**
     * 备注转换器
     */
    private Function<ColumnInfo, String> remarksConverter;

    private boolean alwaysAnnotation;

    {
        typeMapping.put(JdbcType.BIT, Boolean.class);
        typeMapping.put(JdbcType.TINYINT, Byte.class);
        typeMapping.put(JdbcType.SMALLINT, Integer.class);
        typeMapping.put(JdbcType.INTEGER, Integer.class);
        typeMapping.put(JdbcType.BIGINT, Long.class);
        typeMapping.put(JdbcType.FLOAT, Double.class);
        typeMapping.put(JdbcType.REAL, Double.class);
        typeMapping.put(JdbcType.DOUBLE, Double.class);
        typeMapping.put(JdbcType.NUMERIC, BigDecimal.class);
        typeMapping.put(JdbcType.DECIMAL, BigDecimal.class);
        typeMapping.put(JdbcType.CHAR, Character.class);
        typeMapping.put(JdbcType.VARCHAR, String.class);
        typeMapping.put(JdbcType.LONGVARCHAR, String.class);
        typeMapping.put(JdbcType.DATE, LocalDateTime.class);
        typeMapping.put(JdbcType.TIME, LocalTime.class);
        typeMapping.put(JdbcType.TIMESTAMP, LocalDateTime.class);
        typeMapping.put(JdbcType.BINARY, byte[].class);
        typeMapping.put(JdbcType.VARBINARY, byte[].class);
        typeMapping.put(JdbcType.LONGVARBINARY, byte[].class);
        typeMapping.put(JdbcType.BLOB, byte[].class);
        typeMapping.put(JdbcType.CLOB, String.class);
        typeMapping.put(JdbcType.BOOLEAN, Boolean.class);
        typeMapping.put(JdbcType.NVARCHAR, String.class);
        typeMapping.put(JdbcType.NCHAR, String.class);
        typeMapping.put(JdbcType.NCLOB, String.class);
        typeMapping.put(JdbcType.LONGNVARCHAR, String.class);
        typeMapping.put(JdbcType.DATETIMEOFFSET, OffsetDateTime.class);
        typeMapping.put(JdbcType.TIME_WITH_TIMEZONE, OffsetTime.class);
        typeMapping.put(JdbcType.TIMESTAMP_WITH_TIMEZONE, OffsetDateTime.class);
    }

    /**
     * 是否开启 swagger
     *
     * @param enable
     * @return
     */
    public EntityConfig swagger(boolean enable) {
        this.swagger = enable;
        return this;
    }

    /**
     * 是否开启 serial
     *
     * @param enable
     * @return
     */
    public EntityConfig serial(boolean enable) {
        this.serial = enable;
        return this;
    }

    /**
     * 实体类的父类
     *
     * @param superClass
     * @return
     */
    public EntityConfig superClass(String superClass) {
        this.superClass = superClass;
        return this;
    }

    /**
     * 设置列名前缀
     *
     * @param prefixes
     * @return
     */
    public EntityConfig columnPrefixes(String... prefixes) {
        this.columnPrefixes.addAll(Arrays.asList(prefixes));
        return this;
    }

    /**
     * 排除列
     */
    public EntityConfig excludeColumns(String... columns) {
        this.excludeColumns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * 实体类的父类
     *
     * @param superClass
     * @return
     */
    public EntityConfig superClass(Class superClass) {
        this.superClass = superClass.getName();
        return this;
    }

    /**
     * 设置是否使用 lombok
     */
    public EntityConfig lombok(boolean enable) {
        this.lombok = enable;
        return this;
    }

    /**
     * 设置是否使用 lombok @Builder
     */
    public EntityConfig lombokBuilder(boolean enable) {
        this.lombokBuilder = enable;
        return this;
    }

    /**
     * 是否生成默认值
     *
     * @param enable
     * @return
     */
    public EntityConfig defaultValueEnable(boolean enable) {
        this.defaultValueEnable = enable;
        return this;
    }

    /**
     * 设置是否生成 schema
     *
     * @param schema
     * @return
     */
    public EntityConfig schema(boolean schema) {
        this.schema = schema;
        return this;
    }

    /**
     * 包名设置
     *
     * @param packageName
     * @return
     */
    public EntityConfig packageName(String packageName) {
        this.packageName = packageName;
        return this;
    }

    /**
     * 字段类型映射
     *
     * @param consumer
     * @return
     */
    public EntityConfig typeMapping(Consumer<Map<JdbcType, Class<?>>> consumer) {
        consumer.accept(this.typeMapping);
        return this;
    }


    /**
     * 实体类名字转换器
     */
    public EntityConfig nameConvert(Function<String, String> nameConvert) {
        this.nameConvert = nameConvert;
        return this;
    }

    /**
     * 字段名字策略
     */
    public EntityConfig fieldNamingStrategy(NamingStrategy fieldNamingStrategy) {
        this.fieldNamingStrategy = fieldNamingStrategy;
        return this;
    }

    /**
     * 字段名字转换器
     */
    public EntityConfig fieldNameConverter(Function<String, String> fieldNameConverter) {
        this.fieldNameConverter = fieldNameConverter;
        return this;
    }

    /**
     * 备注转换器
     */
    public EntityConfig remarksConverter(Function<ColumnInfo, String> remarksConverter) {
        this.remarksConverter = remarksConverter;
        return this;
    }

    /**
     * 默认TableId代码，数据库非自增时生效
     *
     * @param defaultTableIdCode
     * @return
     */
    public EntityConfig defaultTableIdCode(String defaultTableIdCode) {
        this.defaultTableIdCode = defaultTableIdCode;
        return this;
    }

    /**
     * 设置逻辑删除代码块
     *
     * @param logicDeleteCode
     * @return
     */
    public EntityConfig logicDeleteCode(String logicDeleteCode) {
        this.logicDeleteCode = logicDeleteCode;
        return this;
    }

    public EntityConfig alwaysAnnotation(boolean alwaysAnnotation) {
        this.alwaysAnnotation = alwaysAnnotation;
        return this;
    }

    public boolean isAlwaysAnnotation() {
        return alwaysAnnotation;
    }
}
