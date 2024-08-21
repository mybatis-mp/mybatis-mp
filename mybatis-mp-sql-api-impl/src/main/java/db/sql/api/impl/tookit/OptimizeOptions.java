package db.sql.api.impl.tookit;

import lombok.Data;

@Data
public class OptimizeOptions {

    private boolean optimizeOrderBy = true;

    private boolean optimizeJoin = true;

    public OptimizeOptions optimizeOrderBy(boolean optimizeOrderBy) {
        this.optimizeOrderBy = optimizeOrderBy;
        return this;
    }

    public OptimizeOptions optimizeJoin(boolean optimizeJoin) {
        this.optimizeJoin = optimizeJoin;
        return this;
    }

    public OptimizeOptions disableAll() {
        this.optimizeJoin = false;
        this.optimizeOrderBy = false;
        return this;
    }

    public boolean isAllDisable(){
        return !optimizeOrderBy && !optimizeJoin;
    }
}
