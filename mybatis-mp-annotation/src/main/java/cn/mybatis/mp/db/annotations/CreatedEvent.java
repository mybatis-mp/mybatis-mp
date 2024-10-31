package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CreatedEvent {

    /**
     * 指定类进行消费，需要实现一个onCreatedEvent静态方法,参数为所在直接的VO类
     *
     * @return
     */
    Class<?> value();
}
