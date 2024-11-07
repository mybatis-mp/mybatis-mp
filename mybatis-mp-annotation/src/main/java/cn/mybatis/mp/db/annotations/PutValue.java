package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

/**
 * 此注解会根据值，method，factory做session级的缓存，请注意
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PutValue {

    /**
     * 对应的实体类
     *
     * @return
     */
    Class source() default Void.class;

    /**
     * 对应entity的属性，多个逗号分隔
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
     * 目标类 需要实现 指定method名字的静态方法
     *
     * @return
     */
    Class factory();

    /**
     * @return
     */
    String method();

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
