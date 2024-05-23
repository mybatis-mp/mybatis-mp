package cn.mybatis.mp.routing.datasource;

import java.util.Map;

public class DefaultRoutingDataSourceSwitchContext implements RoutingDataSourceSwitchContext {

    private Map<String, Object> variables;

    @Override
    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

}
