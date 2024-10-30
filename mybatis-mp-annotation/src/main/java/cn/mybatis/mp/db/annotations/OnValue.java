package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface OnValue {

    /**
     * 指定类进行消费，需要实现一个on静态方法
     *
     * @return
     */
    Class<?> value();
}
