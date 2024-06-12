package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.tookit.CmdUtils;

import java.util.*;
import java.util.function.Consumer;


public abstract class BaseExecutor<SELF extends BaseExecutor<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory> implements Executor<SELF, CMD_FACTORY> {

    protected final List<Cmd> cmds = new LinkedList<>();

    private final Map<Class<? extends Cmd>, Integer> cmdSorts = new HashMap<>();

    private DbRunner<SELF> dbRunner;

    private boolean isRunDbRunner = false;

    public BaseExecutor() {
        this.initCmdSorts(cmdSorts);
    }

    private DbRunner<SELF> dbRunner() {
        if (Objects.isNull(this.dbRunner)) {
            this.dbRunner = new DbRunner<>();
        }
        return this.dbRunner;
    }

    @Override
    public SELF onDB(DbType dbType, Consumer<SELF> consumer) {
        this.dbRunner().onDB(dbType, consumer);
        return (SELF) this;
    }

    @Override
    public SELF elseDB(Consumer<SELF> consumer) {
        this.dbRunner().elseDB(consumer);
        return (SELF) this;
    }

    @Override
    public void runOnDB(DbType dbType, SELF executor) {
        if (this.isRunDbRunner) {
            return;
        }
        this.isRunDbRunner = true;
        if (Objects.isNull(this.dbRunner)) {
            return;
        }
        this.dbRunner.runOnDB(dbType, executor);
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
