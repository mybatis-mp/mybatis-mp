package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

/**
 * 分页注解，针对xml，自动帮助你分页
 * 分页有几个要求，
 * 第1个：参数 必须是Pager类
 * 第2个：被注解的方法不能实现
 * 第3个：返回 必须是Pager类
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Paging {

}
