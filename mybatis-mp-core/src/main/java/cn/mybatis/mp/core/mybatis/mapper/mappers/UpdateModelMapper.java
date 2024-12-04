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

import cn.mybatis.mp.core.mybatis.mapper.mappers.utils.UpdateModelMethodUtil;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;
import db.sql.api.impl.cmd.struct.Where;

import java.util.Collection;
import java.util.function.Consumer;

public interface UpdateModelMapper<T> extends BaseMapper<T> {

    /**
     * 实体类修改
     *
     * @param model 实体类对象
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model) {
        return this.update(model, false);
    }

    /**
     * 实体类修改
     *
     * @param model         实体类对象
     * @param allFieldForce 所有字段都强制保存
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, boolean allFieldForce) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, allFieldForce, (Getter[]) null);
    }

    /**
     * 实体类修改
     *
     * @param model
     * @param forceFields 强制更新指定，解决需要修改为null的需求
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, Getter<M>... forceFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, false, forceFields);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    default <M extends Model<T>> int updateModel(Collection<M> list) {
        return this.updateModel(list, false);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list          实体类对象List
     * @param allFieldForce 所有字段都强制保存
     * @return 影响条数
     */
    default <M extends Model<T>> int updateModel(Collection<M> list, boolean allFieldForce) {
        return UpdateModelMethodUtil.update(getBasicMapper(), list, allFieldForce, null);
    }

    /**
     * 多个修改，非批量行为
     *
     * @param list        实体类对象List
     * @param forceFields 强制更新指定，解决需要修改为null的需求
     * @return 影响条数
     */
    default <M extends Model<T>> int updateModel(Collection<M> list, Getter<M>... forceFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), list, false, forceFields);
    }


    /**
     * 动态条件修改
     *
     * @param model    实体类
     * @param consumer where
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, Consumer<Where> consumer) {
        return this.update(model, false, consumer);
    }

    /**
     * 动态条件修改
     *
     * @param model         实体类对象
     * @param allFieldForce 所有字段都强制保存
     * @param consumer      where
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, boolean allFieldForce, Consumer<Where> consumer) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, allFieldForce, null, consumer);
    }

    /**
     * 动态where 修改
     *
     * @param model       实体类对象
     * @param consumer    where
     * @param forceFields 强制更新指定，解决需要修改为null的需求
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, Consumer<Where> consumer, Getter<M>... forceFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, false, forceFields, consumer);
    }

    /**
     * 指定where条件修改
     *
     * @param model 实体类
     * @param where where
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, Where where) {
        return this.update(model, false, where);
    }

    /**
     * 指定where条件修改
     *
     * @param model         实体类对象
     * @param allFieldForce 所有字段都强制保存
     * @param where         where
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, boolean allFieldForce, Where where) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, allFieldForce, (Getter<M>[]) null, where);
    }

    /**
     * 指定where条件修改
     *
     * @param model       实体类对象
     * @param where       where
     * @param forceFields 强制更新指定，解决需要修改为null的需求
     * @return 影响条数
     */
    default <M extends Model<T>> int update(M model, Where where, Getter<M>... forceFields) {
        return UpdateModelMethodUtil.update(getBasicMapper(), model, false, forceFields, where);
    }
}
