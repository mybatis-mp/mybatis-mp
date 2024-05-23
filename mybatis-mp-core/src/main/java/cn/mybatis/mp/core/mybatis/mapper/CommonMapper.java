package cn.mybatis.mp.core.mybatis.mapper;

import cn.mybatis.mp.core.mybatis.mapper.context.EntityInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.ModelInsertContext;
import cn.mybatis.mp.core.mybatis.mapper.context.SQLCmdInsertContext;
import cn.mybatis.mp.core.mybatis.provider.MybatisSQLProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.builder.annotation.ProviderContext;


public interface CommonMapper {

    /**
     * @param insertContext 上下文
     * @return 插入的条数
     * @see MybatisSQLProvider#save(SQLCmdInsertContext, ProviderContext)
     */
    @InsertProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.SAVE_NAME)
    int $save(SQLCmdInsertContext insertContext);


    /**
     * @param insertContext 上下文
     * @return 返回插入的条数
     * @see MybatisSQLProvider#save(SQLCmdInsertContext, ProviderContext)
     */
    @InsertProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.SAVE_NAME)
    int $saveEntity(EntityInsertContext insertContext);


    /**
     * @param insertContext 上下文
     * @return 插入的条数
     * @see MybatisSQLProvider#save(SQLCmdInsertContext, ProviderContext)
     */
    @InsertProvider(type = MybatisSQLProvider.class, method = MybatisSQLProvider.SAVE_NAME)
    int $saveModel(ModelInsertContext insertContext);
}
