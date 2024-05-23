package cn.mybatis.mp.core.mybatis.configuration;

import lombok.Data;

import java.io.Serializable;

@Data
public class FetchObject {

    private final Serializable key;
    private final Object value;

    public FetchObject(Serializable key, Object value) {
        this.key = key;
        this.value = value;
    }
}
