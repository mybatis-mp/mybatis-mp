/*
 *  Copyright (c) 2024-2024, Ai东 (abc-127@live.cn).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License").
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package cn.mybatis.mp.core.mybatis.executor;

import cn.mybatis.mp.core.mybatis.mapping.ResultMapWrapper;
import cn.mybatis.mp.core.mybatis.provider.MybatisSQLProvider;
import cn.mybatis.mp.core.mybatis.provider.SQLCmdSqlSource;
import cn.mybatis.mp.core.util.PagingUtil;
import org.apache.ibatis.builder.annotation.ProviderContext;
import org.apache.ibatis.builder.annotation.ProviderSqlSource;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Method;

public final class MappedStatementUtil {

    public static MappedStatement wrap(MappedStatement ms) {
        PagingUtil.handleMappedStatement(ms);
        ResultMapWrapper.replaceResultMap(ms);
        if (ms.getSqlSource() instanceof ProviderSqlSource) {
            ProviderSqlSource providerSqlSource = (ProviderSqlSource) ms.getSqlSource();
            MetaObject sqlSourceMetaObject = ms.getConfiguration().newMetaObject(providerSqlSource);
            Class<?> providerType = (Class<?>) sqlSourceMetaObject.getValue("providerType");
            if (MybatisSQLProvider.class.isAssignableFrom(providerType)) {
                Method providerMethod = (Method) sqlSourceMetaObject.getValue("providerMethod");
                ProviderContext providerContext = (ProviderContext) sqlSourceMetaObject.getValue("providerContext");
                SQLCmdSqlSource sqlSource = new SQLCmdSqlSource(ms.getConfiguration(), providerMethod, providerContext);
                MetaObject msMetaObject = ms.getConfiguration().newMetaObject(ms);
                msMetaObject.setValue("sqlSource", sqlSource);
            }
        }
        return ms;
    }
}
