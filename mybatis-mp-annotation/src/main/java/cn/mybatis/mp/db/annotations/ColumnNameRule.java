package cn.mybatis.mp.db.annotations;

public enum ColumnNameRule {

    /**
     * 忽略
     */
    IGNORE,

    /**
     * 下划线
     */
    UNDERLINE,

    /**
     * 表示和字段名一样
     */
    USE_FIELD_NAME;
}
