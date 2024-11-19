package cn.mybatis.mp.core.mvc;

import cn.mybatis.mp.db.Model;
import db.sql.api.Getter;

import java.util.Collection;
import java.util.Map;

/**
 * Dao 接口
 *
 * @param <T>
 * @param <K>
 */
public interface Dao<T, K> {
    /**
     * 获取ID的类型
     *
     * @return
     */
    Class<K> getIdType();

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 单个实体
     */
    T getById(K id);

    /**
     * 根据ID查询
     *
     * @param id           ID
     * @param selectFields 指定查询的列
     * @return 单个实体
     */
    T getById(K id, Getter<T>... selectFields);

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
     * @param entity 实体类对象
     * @return 影响条数
     */
    int saveOrUpdate(T entity);

    /**
     * 实体类保存或修改
     *
     * @param entity   实体类对象
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
     * @param list 实体类对象List
     * @return 影响条数
     */
    int saveOrUpdate(Collection<T> list);

    /**
     * 实体类保存或修改
     *
     * @param list     实体类对象List
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
     * @param model 实体类Model对象
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdate(M model);

    /**
     * 实体类Model保存或修改
     *
     * @param model    实体类Model对象
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
     * @param list 实体类Model对象List
     * @return 影响条数
     */
    <M extends Model<T>> int saveOrUpdateModel(Collection<M> list);

    /**
     * 实体类Model保存或修改
     *
     * @param list     实体类Model对象List
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
    int deleteById(K id);

    /**
     * 根据ID删除
     *
     * @param ids 多个ID
     * @return 删除的数量
     */
    int deleteByIds(K... ids);

    /**
     * 根据ID删除
     * @param ids ID 集合
     * @return 删除的数量
     */
    int deleteByIds(Collection<K> ids);

    /**
     * 实体类结果转成Map<ID,T>
     * @param ids 多个ID
     * @return Map结果
     */
    Map<K, T> map(K... ids);

    /**
     * 实体类结果转成Map<ID,T>
     * @param ids ID 集合
     * @return Map结果
     */
    Map<K, T> map(Collection<K> ids);
}
