package db.sql.api.cmd.struct.update;

import db.sql.api.Cmd;

import java.util.List;

public interface IUpdateSets<COLUMN, V, UPDATE_SET_VALUE extends IUpdateSet<COLUMN, V>> extends Cmd {

    List<UPDATE_SET_VALUE> getUpdateSets();


}
