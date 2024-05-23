package cn.mybatis.mp.routing.datasource.test;

import cn.mybatis.mp.routing.datasource.DefaultRoutingDataSourceSwitchContext;
import cn.mybatis.mp.routing.datasource.JdbcConfigDecryptor;
import cn.mybatis.mp.routing.datasource.RoutingDataSourceSwitchContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class Config {

    /**
     * 创建JdbcConfig解密 Bean
     *
     * @return
     */
    @Bean
    JdbcConfigDecryptor jdbcConfigDecryptor() {
        return new TestJdbcConfigDecryptor();
    }

    @Bean
    RoutingDataSourceSwitchContext routingDataSourceSwitchContext() {
        return new DefaultRoutingDataSourceSwitchContext();
    }
}
