package cn.mybatis.mp.db.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Fetch {

    /**
     * Fetch列
     * column 和  property 二选一，column优先
     *
     * @return
     */
    String column() default "";

    /**
     * Fetch 源实体类的属性
     * property + source + storey 组合 用于获取列
     *
     * @return
     */
    String property() default "";

    /**
     * Fetch property 对应的实体类
     *
     * @return
     */
    Class source() default Void.class;

    /**
     * 存储层级
     *
     * @return
     */
    int storey() default 1;

    /**
     * 目标，相当于表
     *
     * @return
     */
    Class target();

    /**
     * 目标属性，相当于关联列 用于条件
     *
     * @return
     */
    String targetProperty();

    /**
     * 目标select属性
     * 用于返回单列的情况
     * 可以动态select 例如:[count({id})] or [{id}+{name} as aa]等
     *
     * @return
     */
    String targetSelectProperty() default "";

    /**
     * 用于结果排序 例如 "xx desc,xx2 desc"; 其中 xx xx2 均为 实体类属性，不是列，多个逗号分割
     * 可以动态 例如:[{id} desc,{createTime} asc]等
     *
     * @return
     */
    String orderBy() default "";

    /**
     * 用于查询group by 例如 "xx,xx2"; 其中 xx xx2 均为 实体类属性，不是列，多个逗号分割
     * 可以动态 例如:[{id} desc,{createTime}]等
     *
     * @return
     */
    String groupBy() default "";

    /**
     * 1 对 1 多条时，发现多条不报错
     *
     * @return
     */
    boolean multiValueErrorIgnore() default false;

    /**
     * 限制条数
     *
     * @return
     */
    int limit() default 0;
}
