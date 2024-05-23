package cn.mybatis.mp.db.annotations;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.UnknownTypeHandler;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableField {

    /**
     * 列名
     *
     * @return
     */
    String value() default "";

    /**
     * 是否进行 select
     * <p>
     * 大字段可设置为 false 不加入 select 查询范围
     *
     * @return 默认 true
     */
    boolean select() default true;

    /**
     * 是否进行 update
     *
     * @return 默认 true
     */
    boolean update() default true;

    /**
     * 配置 列的 jdbcType
     *
     * @return
     */
    JdbcType jdbcType() default JdbcType.UNDEFINED;

    /**
     * 类型处理 针对特殊 类型的列使用
     *
     * @return
     */
    Class<? extends TypeHandler<?>> typeHandler() default UnknownTypeHandler.class;

    /**
     * 默认值 默认为NULL，“”表示NULL，如需表达 空字符，填 {BLANK}
     */
    String defaultValue() default "";

    /**
     * 修改默认值 默认为NULL，“”表示NULL，如需表达 空字符，填 {BLANK}
     */
    String updateDefaultValue() default "";
}
