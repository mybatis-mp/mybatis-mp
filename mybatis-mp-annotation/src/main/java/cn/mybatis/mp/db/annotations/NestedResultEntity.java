package cn.mybatis.mp.db.annotations;


import java.lang.annotation.*;

/**
 * 结果映射
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NestedResultEntity {

    /**
     * 对应的实体类
     *
     * @return
     */
    Class target();

    /**
     * 存储层级，用于自动select场景
     *
     * @return
     */
    int storey() default 1;
}
