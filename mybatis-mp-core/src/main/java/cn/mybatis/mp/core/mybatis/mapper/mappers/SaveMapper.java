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

package cn.mybatis.mp.core.mybatis.mapper.mappers;


import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveBatchStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveStrategy;
import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.SaveMethodUtil;
import cn.mybatis.mp.core.sql.executor.Insert;
import db.sql.api.Getter;

import java.util.Collection;
import java.util.function.Consumer;

public interface SaveMapper<T> extends BaseMapper<T> {

    /**
     * 单个保存
     *
     * @param entity   实体类实例
     * @param consumer 保存策略
     * @return 影响条数
     */
    default int save(T entity, Consumer<SaveStrategy<T>> consumer) {
        SaveStrategy strategy = new SaveStrategy();
        consumer.accept(strategy);
        return SaveMethodUtil.save(getBasicMapper(), getTableInfo(), entity, strategy);
    }

    /**
     * 实体类新增
     *
     * @param entity
     * @return 影响条数
     */
    default int save(T entity) {
        return save(entity, false);
    }

    /**
     * 实体类新增
     *
     * @param entity        实体类实例
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int save(T entity, boolean allFieldForce) {
        return this.save(entity, saveStrategy -> {
            saveStrategy.allFieldSave(allFieldForce);
        });
    }

    /**
     * 实体类新增
     *
     * @param entity      实体类实例
     * @param forceFields 指定那些列强制插入，null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int save(T entity, Getter<T>... forceFields) {
        return this.save(entity, saveStrategy -> {
            saveStrategy.forceFields(forceFields);
        });
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @param consumer 保存策略
     * @return 影响条数
     */
    default int save(Collection<T> list, Consumer<SaveStrategy<T>> saveStrategy) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        SaveStrategy strategy = new SaveStrategy();
        saveStrategy.accept(strategy);
        return SaveMethodUtil.saveList(getBasicMapper(), getTableInfo(), list, strategy);
    }


    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @return 影响条数
     */
    default int save(Collection<T> list) {
        return save(list, false);
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @param allFieldForce 所有字段都强制保存,null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int save(Collection<T> list, boolean allFieldForce) {
        return this.save(list, saveStrategy -> {
            saveStrategy.allFieldSave(allFieldForce);
        });
    }

    /**
     * 多个保存，非批量行为
     *
     * @param list
     * @param forceFields 指定那些列强制插入，null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int save(Collection<T> list, Getter<T>... forceFields) {
        return this.save(list, saveStrategy -> {
            saveStrategy.forceFields(forceFields);
        });
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     *
     * @param list     需要插入数据
     * @param saveBatchStrategy 插入策略
     * @return 影响条数
     */
    default int saveBatch(Collection<T> list, Consumer<SaveBatchStrategy<T>> saveBatchStrategy) {
        SaveBatchStrategy<T> strategy = new SaveBatchStrategy<>();
        saveBatchStrategy.accept(strategy);
        return SaveMethodUtil.saveBatch(getBasicMapper(), new Insert(), getTableInfo(), list, strategy);
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     *
     * @param list
     * @return 影响条数
     */
    default int saveBatch(Collection<T> list) {
        return SaveMethodUtil.saveBatch(getBasicMapper(), list);
    }

    /**
     * 使用数据库原生方式批量插入
     * 一次最好在100条内
     * <p>
     * 会自动加入 主键 租户ID 逻辑删除列 乐观锁
     * 自动设置 默认值,不会忽略NULL值字段
     *
     * @param list
     * @param forceFields 指定那些列强制插入，null值将会以NULL的形式插入
     * @return 影响条数
     */
    default int saveBatch(Collection<T> list, Getter<T>... forceFields) {
        return this.saveBatch(list, saveStrategy -> {
            saveStrategy.forceFields(forceFields);
        });
    }


}
