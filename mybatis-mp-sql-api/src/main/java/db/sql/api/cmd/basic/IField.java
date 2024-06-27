package db.sql.api.cmd.basic;

import db.sql.api.Getter;

public interface IField<T extends IField<T>> extends Alias<T> {

    /**
     * lambda 别名
     *
     * @param aliasGetter
     * @param <T2>
     * @return
     */
    <T2> T as(Getter<T2> aliasGetter);
}
