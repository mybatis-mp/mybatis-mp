package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.tookit.CmdUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public abstract class BaseExecutor<SELF extends BaseExecutor<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory> implements Executor<SELF, CMD_FACTORY> {
    protected final List<Cmd> cmds = new LinkedList<>();

    private final Map<Class<? extends Cmd>, Integer> cmdSorts = new HashMap<>();

    public BaseExecutor() {
        this.initCmdSorts(cmdSorts);
    }


    @Override
    public List<Cmd> cmds() {
        return cmds;
    }

    protected void append(Cmd cmd) {
        cmds.add(cmd);
    }

    @Override
    public Map<Class<? extends Cmd>, Integer> cmdSorts() {
        return cmdSorts;
    }

    abstract void initCmdSorts(Map<Class<? extends Cmd>, Integer> cmdSorts);

    @Override
    public boolean contain(Cmd cmd) {
        return CmdUtils.contain(cmd, this.cmds);
    }
}
