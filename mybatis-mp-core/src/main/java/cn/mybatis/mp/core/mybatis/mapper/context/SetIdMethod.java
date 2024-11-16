package cn.mybatis.mp.core.mybatis.mapper.context;

import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandler;

public interface SetIdMethod {
    /**
     * 设置插入ID
     *
     * @param id ID的值
     * @param index 第几个
     */
    void setId(Object id, int index);

    /**
     * ID是否有值
     * @return 是否有值
     */
    boolean idHasValue();

    /**
     * 插入的个数
     *
     * @return 个数
     */
    int getInsertSize();

    /**
     * 获取ID的TypeHandler
     *
     * @return TypeHandler实例
     */
    TypeHandler<?> getIdTypeHandler(Configuration configuration);
}
