package cn.mybatis.mp.core.tenant;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * 多租户上下文
 */
public class TenantContext {

    private static Supplier<Serializable> tenantInfoGetter;

    private TenantContext() {
    }

    /**
     * 注册多租户获取器
     *
     * @param tenantInfoGetter
     */
    public static void registerTenantGetter(Supplier<Serializable> tenantInfoGetter) {
        TenantContext.tenantInfoGetter = tenantInfoGetter;
    }

    /**
     * 获取租户信息
     *
     * @return
     */
    public static Serializable getTenantId() {
        if (Objects.isNull(tenantInfoGetter)) {
            return null;
        }
        return tenantInfoGetter.get();
    }
}
