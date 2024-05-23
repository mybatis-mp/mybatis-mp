package db.sql.api.impl.cmd.executor;

import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.struct.Where;

public class Update extends AbstractUpdate<Update, CmdFactory> {
    public Update() {
        super(new CmdFactory());
    }

    public Update(Where where) {
        super(where);
    }
}
