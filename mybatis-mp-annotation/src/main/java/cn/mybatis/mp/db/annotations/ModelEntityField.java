package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

/**
 * Model类字段实体类注解 用于解决字段命名不一样问题
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ModelEntityField {

    /**
     * 用于解决名字不一样的问题
     *
     * @return 实体类属性
     */
    String value();

}
