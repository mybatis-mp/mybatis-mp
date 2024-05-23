package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

/**
 * 结果字段 用于解决字段冲突问题
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ResultEntityField {

    /**
     * 对应的实体类
     *
     * @return
     */
    Class target() default Void.class;

    /**
     * 对应target的属性
     * 空时直接去该字段名字
     *
     * @return
     */
    String property() default "";

    /**
     * 存储层级，用于自动select场景
     *
     * @return
     */
    int storey() default 1;
}
