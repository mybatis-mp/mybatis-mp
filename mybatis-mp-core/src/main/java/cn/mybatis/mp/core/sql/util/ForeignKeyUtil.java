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

package cn.mybatis.mp.core.sql.util;

import cn.mybatis.mp.core.db.reflect.ForeignInfo;
import cn.mybatis.mp.core.db.reflect.TableFieldInfo;
import cn.mybatis.mp.core.db.reflect.TableInfo;
import cn.mybatis.mp.core.db.reflect.Tables;
import cn.mybatis.mp.core.sql.MybatisCmdFactory;
import db.sql.api.impl.cmd.struct.On;

import java.util.function.Consumer;

/**
 * 外键util
 */
public final class ForeignKeyUtil {

    private ForeignKeyUtil() {
    }


    /**
     * 构建外键on的Consumer
     *
     * @param mybatisCmdFactory
     * @param mainTable
     * @param mainTableStorey
     * @param secondTable
     * @param secondTableStorey
     * @return
     */
    public static <ON extends On> Consumer<ON> buildForeignKeyOnConsumer(MybatisCmdFactory mybatisCmdFactory, Class mainTable, int mainTableStorey, Class secondTable, int secondTableStorey) {
        return (on) -> {
            TableInfo mainTableInfo = Tables.get(mainTable);
            TableInfo secondTableInfo = Tables.get(secondTable);
            ForeignInfo foreignInfo;
            if ((foreignInfo = secondTableInfo.getForeignInfo(mainTable)) != null) {
                TableFieldInfo foreignFieldInfo = foreignInfo.getTableFieldInfo();
                on.eq(mybatisCmdFactory.field(mainTable, mainTableInfo.getSingleIdFieldInfo(true).getField().getName(), mainTableStorey), mybatisCmdFactory.field(secondTable, foreignFieldInfo.getField().getName(), secondTableStorey));
            } else if ((foreignInfo = mainTableInfo.getForeignInfo(secondTable)) != null) {
                TableFieldInfo foreignFieldInfo = foreignInfo.getTableFieldInfo();
                on.eq(mybatisCmdFactory.field(secondTable, secondTableInfo.getSingleIdFieldInfo(true).getField().getName(), secondTableStorey), mybatisCmdFactory.field(mainTable, foreignFieldInfo.getField().getName(), mainTableStorey));
            }
        };
    }
}
