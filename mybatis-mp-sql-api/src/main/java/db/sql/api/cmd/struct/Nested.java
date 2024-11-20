package db.sql.api.cmd.struct;

import java.util.function.Consumer;

public interface Nested<SELF, CHAIN> {

    SELF andNested(Consumer<CHAIN> consumer);

    SELF orNested(Consumer<CHAIN> consumer);

    default SELF andNested(boolean when,Consumer<CHAIN> consumer){
        if(!when){
            return (SELF) this;
        }
        return andNested(consumer);
    }

    default SELF orNested(boolean when,Consumer<CHAIN> consumer){
        if(!when){
            return (SELF) this;
        }
        return orNested(consumer);
    }
}
