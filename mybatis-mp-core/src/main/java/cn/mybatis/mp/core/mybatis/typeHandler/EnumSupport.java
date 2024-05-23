package cn.mybatis.mp.core.mybatis.typeHandler;

import java.io.Serializable;

/**
 * 枚举支持，需要用一个枚举 进行 实现
 *
 * @param <T>
 */
public interface EnumSupport<T> extends Serializable {

    T getCode();
}
