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

package cn.mybatis.mp.core.mvc;

import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveBatchStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveOrUpdateStrategy;
import cn.mybatis.mp.core.mybatis.mapper.context.strategy.SaveStrategy;
import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Dao 接口
 *
 * @param <T>
 * @param <ID>
 */
public interface Dao<T, ID> {

    /**
     * 获取ID的类型
     *
     * @return
     */
    Class<ID> getIdType();

    /**
     * 获取ID的类型
     *
     * @return
     */
    Class<T> getEntityType();

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 单个实体
     */
    T getById(ID id);

    /**
     * 根据ID查询
     *
     * @param id           ID
     * @param selectFields 指定查询的列
     * @return 单个实体
     */
    T getById(ID id, Getter<T>... selectFields);

    /**
     * 实体类保存
     *
     * @param entity 实体类对象
     * @return 影响条数
     */
    int save(T entity);

    /**
     * 实体类保存
     *
     * @param entity      实体类对象
     * @param forceFields 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    int save(T entity, Getter<T>... forceFields);

    /**
     * 实体类保存
     *
     * @param entity        实体类对象
     * @param allFieldForce 是否所有字段都插入，如果是null值，则变成NULL
     * @return 影响条数
     */
    int save(T entity, boolean allFieldForce);

    /**
     * 实体类保存
     *
     * @param entity       实体类对象
     * @param saveStrategy 保存策略
     * @return 影响条数
     */
    int save(T entity, Consumer<SaveStrategy<T>> saveStrategy);

    /**
     * 实体类保存
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    int save(Collection<T> list);

    /**
     * 实体类保存
     *
     * @param list          实体类对象List
     * @param allFieldForce 是否所有字段都插入，如果是null值，则变成NULL
     * @return 影响条数
     */
    int save(Collection<T> list, boolean allFieldForce);

    /**
     * 实体类保存
     *
     * @param list        实体类对象List
     * @param forceFields 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    int save(Collection<T> list, Getter<T>... forceFields);

    /**
     * 实体类保存
     *
     * @param list         实体类对象List
     * @param saveStrategy 保存策略
     * @return 影响条数
     */
    int save(Collection<T> list, Consumer<SaveStrategy<T>> saveStrategy);

    /**
     * 实体类Model保存
     *
     * @param model 实体类Model对象
     * @return 影响条数
     */
    <M extends Model<T>> int save(M model);

    /**
     * 实体类Model保存
     *
     * @param model       实体类Model对象
     * @param forceFields 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int save(M model, Getter<M>... forceFields);

    /**
     * 实体类Model保存
     *
     * @param model         实体类Model对象
     * @param allFieldForce 是否所有字段都插入，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int save(M model, boolean allFieldForce);

    /**
     * 实体类Model保存
     *
     * @param model        实体类Model对象
     * @param saveStrategy 保存策略
     * @return 影响条数
     */
    <M extends Model<T>> int save(M model, Consumer<SaveStrategy<M>> saveStrategy);

    /**
     * 实体类Model保存
     *
     * @param list 实体类Model对象List
     * @return 影响条数
     */
    <M extends Model<T>> int saveModel(Collection<M> list);

    /**
     * 实体类Model保存
     *
     * @param list          实体类Model对象List
     * @param allFieldForce 是否所有字段都插入，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int saveModel(Collection<M> list, boolean allFieldForce);

    /**
     * 实体类Model保存
     *
     * @param list        实体类Model对象List
     * @param forceFields 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int saveModel(Collection<M> list, Getter<M>... forceFields);

    /**
     * 实体类Model保存
     *
     * @param list         实体类Model对象List
     * @param saveStrategy 保存策略
     * @return 影响条数
     */
    <M extends Model<T>> int saveModel(Collection<M> list, Consumer<SaveStrategy<M>> saveStrategy);


    /**
     * 原生批量插入 实体类Model保存
     *
     * @param list 实体类Model对象List
     * @return 影响条数
     */
    <M extends Model<T>> int saveModelBatch(Collection<M> list);

    /**
     * 原生批量插入 实体类Model保存
     *
     * @param list              实体类Model对象List
     * @param saveBatchStrategy 保存策略
     * @return 影响条数
     */
    <M extends Model<T>> int saveModelBatch(Collection<M> list, Consumer<SaveBatchStrategy<M>> saveBatchStrategy);

    /**
     * 原生批量插入
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    int saveBatch(Collection<T> list);

    /**
     * 原生批量插入
     *
     * @param list              实体类对象List
     * @param saveBatchStrategy 策略
     * @return 影响条数
     */
    int saveBatch(Collection<T> list, Consumer<SaveBatchStrategy<T>> saveBatchStrategy);
    /**
     * 实体类修改
     *
     * @param entity 实体类对象
     * @return 影响条数
     */
    int update(T entity);

    /**
     * 实体类修改
     *
     * @param entity        实体类对象
     * @param allFieldForce 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    int update(T entity, boolean allFieldForce);

    /**
     * 实体类修改
     *
     * @param entity      实体类对象
     * @param forceFields 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    int update(T entity, Getter<T>... forceFields);

    /**
     * 实体类修改
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    int update(Collection<T> list);

    /**
     * 实体类修改
     *
     * @param list          实体类对象List
     * @param allFieldForce 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    int update(Collection<T> list, boolean allFieldForce);

    /**
     * 实体类修改
     *
     * @param list        实体类对象List
     * @param forceFields 强制修改的字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    int update(Collection<T> list, Getter<T>... forceFields);


    /**
     * 实体类Model修改
     *
     * @param model 实体类Model对象
     * @return 影响条数
     */
    <M extends Model<T>> int update(M model);

    /**
     * 实体类Model修改
     *
     * @param model       实体类Model对象
     * @param forceFields 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int update(M model, Getter<M>... forceFields);

    /**
     * 实体类Model修改
     *
     * @param model         实体类Model对象
     * @param allFieldForce 是否所有字段都插入，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int update(M model, boolean allFieldForce);

    /**
     * 实体类Model修改
     *
     * @param list 实体类Model对象List
     * @return 影响条数
     */
    <M extends Model<T>> int updateModel(Collection<M> list);

    /**
     * 实体类Model修改
     *
     * @param list        实体类Model对象List
     * @param forceFields 强制字段，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int updateModel(Collection<M> list, Getter<M>... forceFields);

    /**
     * 实体类Model修改
     *
     * @param list          实体类Model对象List
     * @param allFieldForce 是否所有字段都插入，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int updateModel(Collection<M> list, boolean allFieldForce);

    /**
     * 实体类保存或修改
     *
     * @param entity               实体类对象
     * @param saveOrUpdateStrategy 策略
     * @return 影响条数
     */
    int saveOrUpdate(T entity, Consumer<SaveOrUpdateStrategy<T>> saveOrUpdateStrategy);
    /**
     * 实体类保存或修改
     *
     * @param entity 实体类对象
     * @return 影响条数
     */
    int saveOrUpdate(T entity);

    /**
     * 实体类保存或修改
     *
     * @param entity        实体类对象
     * @param allFieldForce 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    int saveOrUpdate(T entity, boolean allFieldForce);

    /**
     * 实体类保存或修改
     *
     * @param entity      实体类对象
     * @param forceFields 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    int saveOrUpdate(T entity, Getter<T>... forceFields);

    /**
     * 实体类保存或修改
     *
     * @param list                 实体类对象List
     * @param saveOrUpdateStrategy
     * @return
     */
    int saveOrUpdate(Collection<T> list, Consumer<SaveOrUpdateStrategy<T>> saveOrUpdateStrategy);

    /**
     * 实体类保存或修改
     *
     * @param list 实体类对象List
     * @return 影响条数
     */
    int saveOrUpdate(Collection<T> list);

    /**
     * 实体类保存或修改
     *
     * @param list          实体类对象List
     * @param allFieldForce 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    int saveOrUpdate(Collection<T> list, boolean allFieldForce);

    /**
     * 实体类保存或修改
     *
     * @param list        实体类对象List
     * @param forceFields 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    int saveOrUpdate(Collection<T> list, Getter<T>... forceFields);


    /**
     * 实体类Model保存或修改
     *
     * @param model                实体类Model对象
     * @param saveOrUpdateStrategy 策略
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdate(M model, Consumer<SaveOrUpdateStrategy<M>> saveOrUpdateStrategy);

    /**
     * 实体类Model保存或修改
     *
     * @param model 实体类Model对象
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdate(M model);

    /**
     * 实体类Model保存或修改
     *
     * @param model         实体类Model对象
     * @param allFieldForce 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdate(M model, boolean allFieldForce);

    /**
     * 实体类Model保存或修改
     *
     * @param model       实体类Model对象
     * @param forceFields 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdate(M model, Getter<M>... forceFields);


    /**
     * 实体类Model保存或修改
     *
     * @param list                 实体类Model对象List
     * @param saveOrUpdateStrategy 策略
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, Consumer<SaveOrUpdateStrategy<M>> saveOrUpdateStrategy);

    /**
     * 实体类Model保存或修改
     *
     * @param list 实体类Model对象List
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdateModel(Collection<M> list);

    /**
     * 实体类Model保存或修改
     *
     * @param list          实体类Model对象List
     * @param allFieldForce 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, boolean allFieldForce);

    /**
     * 实体类Model保存或修改
     *
     * @param list        实体类Model对象List
     * @param forceFields 是否所有字段都保存或修改，如果是null值，则变成NULL
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdateModel(Collection<M> list, Getter<M>... forceFields);

    /**
     * 实体类删除
     *
     * @param entity 实体类对象
     * @return 删除的数量
     */
    int delete(T entity);

    /**
     * 实体类删除
     *
     * @param list 实体类对象List
     * @return 删除的数量
     */
    int delete(Collection<T> list);

    /**
     * 根据ID删除
     *
     * @param id ID
     * @return 删除的数量
     */
    int deleteById(ID id);

    /**
     * 根据ID删除
     *
     * @param ids 多个ID
     * @return 删除的数量
     */
    int deleteByIds(ID... ids);

    /**
     * 根据ID删除
     *
     * @param ids ID 集合
     * @return 删除的数量
     */
    int deleteByIds(Collection<ID> ids);

    /**
     * 实体类结果转成Map<ID,T>
     *
     * @param ids 多个ID
     * @return Map结果
     */
    Map<ID, T> map(ID... ids);

    /**
     * 实体类结果转成Map<ID,T>
     *
     * @param ids ID 集合
     * @return Map结果
     */
    Map<ID, T> map(Collection<ID> ids);
}
