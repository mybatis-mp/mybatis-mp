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

package cn.mybatis.mp.core.mybatis.mapper.context;

import cn.mybatis.mp.core.db.reflect.ModelFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.sql.util.IdValueConverter;
import cn.mybatis.mp.core.util.ModelInfoUtil;
import cn.mybatis.mp.core.util.StringPool;
import cn.mybatis.mp.core.util.TableInfoUtil;
import org.apache.ibatis.reflection.invoker.GetFieldInvoker;

import java.util.Objects;

public final class IdUtil {

    public static boolean isIdExists(Object obj, TableFieldInfo idFieldInfo) {
        if (idFieldInfo == null) {
            return false;
        }
        Object sourceId;
        try {
            sourceId = idFieldInfo.getReadFieldInvoker().invoke(obj, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return isIdValueExists(sourceId);
    }

    public static boolean isIdExists(Object obj, ModelFieldInfo idFieldInfo) {
        Object sourceId;
        try {
            sourceId = idFieldInfo.getReadFieldInvoker().invoke(obj, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return isIdValueExists(sourceId);
    }

    private static boolean isIdExists(Object obj, GetFieldInvoker getFieldInvoker) {
        //如果设置了id 则不在设置
        Object sourceId;
        try {
            sourceId = getFieldInvoker.invoke(obj, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return isIdValueExists(sourceId);
    }

    public static boolean isIdValueExists(Object id) {
        if (Objects.nonNull(id)) {
            if (id instanceof String) {
                return !StringPool.EMPTY.equals(((String) id).trim());
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean setId(Object obj, TableFieldInfo idFieldInfo, Object id) {
//        if (isIdExists(obj, idFieldInfo.getReadFieldInvoker())) {
//            return false;
//        }

        id = IdValueConverter.convert(id, idFieldInfo.getFieldInfo().getTypeClass());
        TableInfoUtil.setValue(idFieldInfo, obj, id);
        return true;
    }

    public static boolean setId(Object obj, ModelFieldInfo idFieldInfo, Object id) {
//        if (isIdExists(obj, idFieldInfo.getReadFieldInvoker())) {
//            return false;
//        }

        if (idFieldInfo.getFieldInfo().getTypeClass() == String.class) {
            id = id instanceof String ? id : String.valueOf(id);
        }
        id = IdValueConverter.convert(id, idFieldInfo.getFieldInfo().getTypeClass());
        //默认值回写
        ModelInfoUtil.setValue(idFieldInfo, obj, id);
        return true;
    }
}
