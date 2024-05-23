package cn.mybatis.mp.routing.datasource;

import cn.mybatis.mp.routing.datasource.dataSourceConfig.ConfigType;
import cn.mybatis.mp.routing.datasource.dataSourceConfig.DruidConfig;
import cn.mybatis.mp.routing.datasource.dataSourceConfig.HikariConfig;

import java.util.Properties;

public class DataSourceProperties extends org.springframework.boot.autoconfigure.jdbc.DataSourceProperties {

    /**
     * 配置的类型
     */
    private ConfigType configType;

    /**
     * hikari 数据连接池配置
     */
    private HikariConfig hikari;

    /**
     * druid 数据连接池配置
     */
    private DruidConfig druid;

    /**
     * other 数据连接池配置
     */
    private Properties other;

    public ConfigType getConfigType() {
        return configType;
    }

    public HikariConfig getHikari() {
        return hikari;
    }

    public void setHikari(HikariConfig hikari) {
        this.hikari = hikari;
        this.configType = ConfigType.HIKARI;
    }

    public DruidConfig getDruid() {
        return druid;
    }

    public void setDruid(DruidConfig druid) {
        this.druid = druid;
        this.configType = ConfigType.DRUID;
    }

    public Properties getOther() {
        return other;
    }

    public void setOther(Properties other) {
        this.other = other;
        this.configType = ConfigType.OTHER;
    }
}
