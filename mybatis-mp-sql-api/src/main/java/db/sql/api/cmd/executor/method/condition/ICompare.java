package db.sql.api.cmd.executor.method.condition;

import db.sql.api.cmd.LikeMode;
import db.sql.api.cmd.executor.method.condition.compare.*;

import java.io.Serializable;

/**
 * 比较器
 *
 * @param <RV>     返回
 * @param <COLUMN> 列
 * @param <V>      比较值
 */
public interface ICompare<RV, COLUMN, V> extends
        IEqGetterCompare<RV, V>,
        IEqGetterPredicateCompare<RV, V>,
        INeGetterCompare<RV, V>,
        INeGetterPredicateCompare<RV, V>,
        IGtGetterCompare<RV, V>,
        IGtGetterPredicateCompare<RV, V>,
        IGteGetterCompare<RV, V>,
        IGteGetterPredicateCompare<RV, V>,
        ILtGetterCompare<RV, V>,
        ILtGetterPredicateCompare<RV, V>,
        ILteGetterCompare<RV, V>,
        ILteGetterPredicateCompare<RV, V>,
        ILikeGetterCompare<RV>,
        ILikeGetterPredicateCompare<RV>,
        INotLikeGetterCompare<RV>,
        INotLikeGetterPredicateCompare<RV>,
        IBetweenGetterCompare<RV>,
        IBetweenGetterPredicateCompare<RV>,
        INotBetweenGetterCompare<RV>,
        INotBetweenGetterPredicateCompare<RV>,
        IIsNullGetterCompare<RV>,
        IIsNotNullGetterCompare<RV>,
        IEmptyGetterCompare<RV>,
        INotEmptyGetterCompare<RV> {

    RV empty(COLUMN column);

    RV notEmpty(COLUMN column);

    RV eq(COLUMN column, V value);

    RV ne(COLUMN column, V value);

    RV gt(COLUMN column, V value);

    RV gte(COLUMN column, V value);

    RV lt(COLUMN column, V value);

    RV lte(COLUMN column, V value);

    default RV like(COLUMN column, String value) {
        return this.like(LikeMode.DEFAULT, column, value);
    }

    RV like(LikeMode mode, COLUMN column, String value);

    default RV notLike(COLUMN column, String value) {
        return this.notLike(LikeMode.DEFAULT, column, value);
    }

    RV notLike(LikeMode mode, COLUMN column, String value);

    RV between(COLUMN column, Serializable value, Serializable value2);

    RV notBetween(COLUMN column, Serializable value, Serializable value2);

    RV isNull(COLUMN column);

    RV isNotNull(COLUMN column);
}
