package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

/**
 * 用于vo上自定义 TypeHandler
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TypeHandler {

    Class<? extends org.apache.ibatis.type.TypeHandler<?>> value();
}
