package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PutEnumValue {

    /**
     * 对应的实体类
     *
     * @return
     */
    Class source() default Void.class;

    /**
     * 对应entity的属性
     *
     * @return
     */
    String property();

    /**
     * 存储层级，用于自动select场景
     *
     * @return
     */
    int storey() default 1;

    /**
     * 枚举类
     *
     * @return
     */
    Class target();

    /**
     * 枚举code字段名字
     *
     * @return
     */
    String code() default "code";

    /**
     * 枚举值的字段名字
     *
     * @return
     */
    String value() default "name";

    /**
     * 是否必须有值
     *
     * @return
     */
    boolean required() default false;

    /**
     * 未匹配时的默认值
     *
     * @return
     */
    String defaultValue() default "";
}
