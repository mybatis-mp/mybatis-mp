package db.sql.api.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.DbType;

public interface IDatasetField<T extends IDatasetField<T>> extends IField<T>, Cmd {

    /**
     * 数据集对象 表或子表
     *
     * @return
     */
    IDataset getTable();

    /**
     * 字段名字
     *
     * @return
     */
    String getName();

    /**
     * 根据dbType处理后的对象
     *
     * @param dbType
     * @return
     */
    String getName(DbType dbType);

}
