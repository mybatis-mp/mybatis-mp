package cn.mybatis.mp.core.mybatis.configuration;

import lombok.Data;

import java.io.Serializable;

@Data
public class FetchObject {

    private final Serializable key;

    private final Object value;

    private final Object sourceKey;

    public FetchObject(Object sourceKey, Serializable key, Object value) {
        this.sourceKey = sourceKey;
        this.key = key;
        this.value = value;
    }
}
