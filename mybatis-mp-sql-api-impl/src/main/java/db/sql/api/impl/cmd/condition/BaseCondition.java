package db.sql.api.impl.cmd.condition;


import db.sql.api.impl.cmd.basic.Condition;

public abstract class BaseCondition<COLUMN, V> implements Condition<COLUMN, V> {

    private final char[] operator;

    public BaseCondition(char[] operator) {
        this.operator = operator;
    }

    public char[] getOperator() {
        return operator;
    }
}
