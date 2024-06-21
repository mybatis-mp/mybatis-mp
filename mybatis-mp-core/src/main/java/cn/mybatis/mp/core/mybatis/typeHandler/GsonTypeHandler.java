package cn.mybatis.mp.core.mybatis.typeHandler;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Objects;

public class GsonTypeHandler extends AbstractJsonTypeHandler {

    private volatile static Gson GSON;

    public GsonTypeHandler(Class<?> type) {
        super(type);
    }

    public GsonTypeHandler(Class<?> type, Type genericType) {
        super(type, genericType);
    }

    public static Gson getGson() {
        if (null == GSON) {
            GSON = new Gson();
        }

        return GSON;
    }

    public static void setGson(Gson gson) {
        Objects.requireNonNull(gson);
        GSON = gson;
    }

    @Override
    String toJson(Object obj) {
        return getGson().toJson(obj);
    }

    @Override
    Object parseJson(String json) {
        return getGson().fromJson(json, this.getDeserializeType());
    }
}
