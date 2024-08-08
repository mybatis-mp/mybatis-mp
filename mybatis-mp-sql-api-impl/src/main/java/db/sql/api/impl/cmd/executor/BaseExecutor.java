package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.tookit.CmdUtils;

import java.util.*;
import java.util.function.BiConsumer;


public abstract class BaseExecutor<SELF extends BaseExecutor<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory> implements Executor<SELF, CMD_FACTORY> {

    protected final List<Cmd> cmds = new ArrayList<>();

    private final Map<Class<? extends Cmd>, Integer> cmdSorts = new HashMap<>();

    private List<Selector> selectors;

    private boolean isExecuteSelector = false;

    public BaseExecutor() {
        this.initCmdSorts(cmdSorts);
    }

    private Selector createSelector() {
        if (Objects.isNull(this.selectors)) {
            this.selectors = new ArrayList<>();
        }
        DbSelector dbSelector = new DbSelector();
        this.selectors.add(dbSelector);
        return dbSelector;
    }

    @Override
    public SELF dbAdapt(BiConsumer<SELF, Selector> consumer) {
        SELF self = (SELF) this;
        consumer.accept(self, this.createSelector());
        return (SELF) this;
    }

    @Override
    public void selectorExecute(DbType dbType) {
        if (this.isExecuteSelector) {
            return;
        }
        this.isExecuteSelector = true;
        if (Objects.isNull(this.selectors)) {
            return;
        }
        selectors.forEach(dbSelector -> dbSelector.dbExecute(dbType));
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
