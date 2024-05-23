package cn.mybatis.mp.generator.config;

public enum ContainerType {

    SPRING, SOLON;

    public boolean is(String type) {
        return this.name().equalsIgnoreCase(type);
    }
}
