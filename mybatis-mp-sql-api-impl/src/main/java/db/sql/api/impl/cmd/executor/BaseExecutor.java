package db.sql.api.impl.cmd.executor;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.impl.cmd.CmdFactory;
import db.sql.api.tookit.CmdUtils;

import java.util.*;
import java.util.function.BiConsumer;


public abstract class BaseExecutor<SELF extends BaseExecutor<SELF, CMD_FACTORY>, CMD_FACTORY extends CmdFactory> implements Executor<SELF, CMD_FACTORY> {

    protected final List<Cmd> cmds = new LinkedList<>();

    private final Map<Class<? extends Cmd>, Integer> cmdSorts = new HashMap<>();

    private List<DbSelector> dbSelectors;

    private boolean isExecuteSelector = false;

    public BaseExecutor() {
        this.initCmdSorts(cmdSorts);
    }

    private DbSelector createDbSelector() {
        if (Objects.isNull(this.dbSelectors)) {
            this.dbSelectors = new ArrayList<>();
        }
        DbSelector dbSelector = new DbSelector();
        this.dbSelectors.add(dbSelector);
        return dbSelector;
    }

    @Override
    public SELF dbExecutor(BiConsumer<SELF, DbSelector> consumer) {
        SELF self = (SELF) this;
        consumer.accept(self, this.createDbSelector());
        return (SELF) this;
    }

    @Override
    public void dbExecute(DbType dbType) {
        if (this.isExecuteSelector) {
            return;
        }
        this.isExecuteSelector = true;
        if (Objects.isNull(this.dbSelectors)) {
            return;
        }
        dbSelectors.stream().forEach(dbSelector -> dbSelector.dbExecute(dbType));
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
