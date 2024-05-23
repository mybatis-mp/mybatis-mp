package db.sql.api.impl.cmd.executor;

import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.impl.cmd.struct.Where;

public class Delete extends AbstractDelete<Delete, CmdFactory> {

    public Delete() {
        super(new CmdFactory());
    }

    public Delete(Where where) {
        super(where);
    }
}
