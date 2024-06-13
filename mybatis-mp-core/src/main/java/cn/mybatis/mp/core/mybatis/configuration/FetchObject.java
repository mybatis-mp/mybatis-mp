package cn.mybatis.mp.core.mybatis.configuration;

import lombok.Data;

@Data
public class FetchObject {

    private final String matchKey;

    private final Object value;

    private final Object sourceKey;

    public FetchObject(Object sourceKey, String matchKey, Object value) {
        this.sourceKey = sourceKey;
        this.matchKey = matchKey;
        this.value = value;
    }
}
