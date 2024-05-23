package db.sql.api.cmd.struct;

import db.sql.api.Cmd;

public interface IForUpdate<SELF extends IForUpdate> extends Cmd {
    void setWait(boolean wait);
}
