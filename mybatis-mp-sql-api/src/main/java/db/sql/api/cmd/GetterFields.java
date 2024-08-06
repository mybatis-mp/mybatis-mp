package db.sql.api.cmd;

import db.sql.api.Getter;

/**
 * GetterField 数组
 */
public final class GetterFields {

    public <T> GetterField[] of(Getter<T>... getters) {
        GetterField[] getterFields = new GetterField[getters.length];
        for (int i = 0; i < getters.length; i++) {
            getterFields[i] = new GetterField(getters[i]);
        }
        return getterFields;
    }

    public <T1> GetterField[] of(Getter<T1> getter1) {
        return new GetterField[]{GetterField.create(getter1)};
    }

    public <T1> GetterField[] of(Getter<T1> getter1, int storey1) {
        return new GetterField[]{GetterField.create(getter1, storey1)};
    }

    public <T1, T2> GetterField[] of(Getter<T1> getter1, Getter<T2> getter2) {
        return new GetterField[]{GetterField.create(getter1), GetterField.create(getter2)};
    }

    public <T1, T2> GetterField[] of(Getter<T1> getter1, int storey1, Getter<T2> getter2, int storey2) {
        return new GetterField[]{GetterField.create(getter1, storey1), GetterField.create(getter2, storey2)};
    }

    public <T1, T2, T3> GetterField[] of(Getter<T1> getter1, Getter<T2> getter2, Getter<T3> getter3) {
        return new GetterField[]{GetterField.create(getter1), GetterField.create(getter2), GetterField.create(getter3)};
    }

    public <T1, T2, T3> GetterField[] of(Getter<T1> getter1, int storey1, Getter<T2> getter2, int storey2, Getter<T3> getter3, int storey3) {
        return new GetterField[]{GetterField.create(getter1, storey1), GetterField.create(getter2, storey2), GetterField.create(getter3, storey3)};
    }

    public <T1, T2, T3, T4> GetterField[] of(Getter<T1> getter1, Getter<T2> getter2, Getter<T3> getter3, Getter<T4> getter4) {
        return new GetterField[]{GetterField.create(getter1), GetterField.create(getter2), GetterField.create(getter3), GetterField.create(getter4)};
    }

    public <T1, T2, T3, T4> GetterField[] of(Getter<T1> getter1, int storey1, Getter<T2> getter2, int storey2, Getter<T3> getter3, int storey3, Getter<T4> getter4, int storey4) {
        return new GetterField[]{GetterField.create(getter1, storey1), GetterField.create(getter2, storey2), GetterField.create(getter3, storey3), GetterField.create(getter4, storey4)};
    }

    public <T1, T2, T3, T4, T5> GetterField[] of(Getter<T1> getter1, Getter<T2> getter2, Getter<T3> getter3, Getter<T4> getter4, Getter<T5> getter5) {
        return new GetterField[]{GetterField.create(getter1), GetterField.create(getter2), GetterField.create(getter3), GetterField.create(getter4), GetterField.create(getter5)};
    }

    public <T1, T2, T3, T4, T5> GetterField[] of(Getter<T1> getter1, int storey1, Getter<T2> getter2, int storey2, Getter<T3> getter3, int storey3, Getter<T4> getter4, int storey4, Getter<T5> getter5, int storey5) {
        return new GetterField[]{GetterField.create(getter1, storey1), GetterField.create(getter2, storey2), GetterField.create(getter3, storey3), GetterField.create(getter4, storey4), GetterField.create(getter5, storey5)};
    }

    public <T1, T2, T3, T4, T5, T6> GetterField[] of(Getter<T1> getter1, Getter<T2> getter2, Getter<T3> getter3, Getter<T4> getter4, Getter<T5> getter5, Getter<T6> getter6) {
        return new GetterField[]{GetterField.create(getter1), GetterField.create(getter2), GetterField.create(getter3), GetterField.create(getter4), GetterField.create(getter5), GetterField.create(getter6)};
    }

    public <T1, T2, T3, T4, T5, T6> GetterField[] of(Getter<T1> getter1, int storey1, Getter<T2> getter2, int storey2, Getter<T3> getter3, int storey3, Getter<T4> getter4, int storey4, Getter<T5> getter5, int storey5, Getter<T6> getter6, int storey6) {
        return new GetterField[]{GetterField.create(getter1, storey1), GetterField.create(getter2, storey2), GetterField.create(getter3, storey3), GetterField.create(getter4, storey4), GetterField.create(getter5, storey5), GetterField.create(getter6, storey6)};
    }
}
