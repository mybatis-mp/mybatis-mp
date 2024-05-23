package cn.mybatis.mp.core.db.reflect;

import cn.mybatis.mp.db.annotations.NestedResultEntity;

import java.lang.reflect.Field;
import java.util.List;

public class NestedResultInfo {

    /**
     * 内嵌信息
     */
    public final List<NestedResultInfo> nestedResultInfos;
    private final Field field;
    /**
     * 目标实体类
     */
    private final Class<?> targetEntityType;
    /**
     * 实体类存储层级
     */
    private final int storey;
    /**
     * 所有的 ResultFieldInfo 不包括内嵌的
     */
    private final List<ResultFieldInfo> resultFieldInfos;

    public NestedResultInfo(Field field, NestedResultEntity nestedResultEntity, List<ResultFieldInfo> resultFieldInfos, List<NestedResultInfo> nestedResultInfos) {
        this.field = field;
        this.targetEntityType = nestedResultEntity.target();
        this.storey = nestedResultEntity.storey();
        this.resultFieldInfos = resultFieldInfos;
        this.nestedResultInfos = nestedResultInfos;
    }

    public Field getField() {
        return field;
    }

    public Class<?> getTargetEntityType() {
        return targetEntityType;
    }

    public List<ResultFieldInfo> getResultFieldInfos() {
        return resultFieldInfos;
    }

    public List<NestedResultInfo> getNestedResultInfos() {
        return nestedResultInfos;
    }
}
