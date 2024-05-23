package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.core.NotTableClassException;
import cn.mybatis.mp.db.annotations.ResultEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResultInfos {

    private static final Map<Class, ResultInfo> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取结果映射类的的信息
     *
     * @param clazz
     * @return
     */

    public static ResultInfo get(Class clazz) {
        if (!clazz.isAnnotationPresent(ResultEntity.class)) {
            throw new NotTableClassException(clazz);
        }
        if (CACHE.containsKey(clazz)) {
            return CACHE.get(clazz);
        }
        synchronized (clazz) {
            if (CACHE.containsKey(clazz)) {
                return CACHE.get(clazz);
            }
            ResultInfo resultInfo = new ResultInfo(clazz);
            CACHE.put(clazz, resultInfo);
            return resultInfo;
        }
    }

}
