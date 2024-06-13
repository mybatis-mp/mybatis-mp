package db.sql.api.impl.cmd.executor;

import db.sql.api.DbType;
import db.sql.api.cmd.executor.DBRunnable;
import db.sql.api.impl.tookit.Objects;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class DbRunner<T> implements DBRunnable<DbRunner<T>, T> {

    private final Map<DbType, Consumer<T>> consumerMap = new HashMap<>();

    private Consumer<T> elseConsumer;


    @Override
    public DbRunner<T> onDB(Consumer<T> consumer, DbType... dbTypes) {
        for (DbType dbType : dbTypes) {
            consumerMap.put(dbType, consumer);
        }
        return this;
    }

    @Override
    public DbRunner<T> elseDB(Consumer<T> consumer) {
        this.elseConsumer = consumer;
        return null;
    }

    @Override
    public void runOnDB(DbType dbType, T t) {
        Consumer<T> consumer = consumerMap.get(dbType);
        if (Objects.nonNull(consumer)) {
            consumer.accept(t);
            return;
        }
        if (Objects.nonNull(this.elseConsumer)) {
            this.elseConsumer.accept(t);
            return;
        }

        throw new RuntimeException("Not adapted to DbType" + dbType);
    }
}
