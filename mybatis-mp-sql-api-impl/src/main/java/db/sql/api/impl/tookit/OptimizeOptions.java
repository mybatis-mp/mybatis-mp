package db.sql.api.impl.tookit;

public class OptimizeOptions {

    /**
     * 是否优化OrderBy
     */
    private boolean optimizeOrderBy = true;

    /**
     * 是否优化Join
     */
    private boolean optimizeJoin = true;

    /**
     * 设置是否优化OrderBy
     *
     * @param optimizeOrderBy
     * @return OptimizeOptions
     */
    public OptimizeOptions optimizeOrderBy(boolean optimizeOrderBy) {
        this.optimizeOrderBy = optimizeOrderBy;
        return this;
    }

    /**
     * 设置是否优化Join
     *
     * @param optimizeJoin
     * @return OptimizeOptions
     */
    public OptimizeOptions optimizeJoin(boolean optimizeJoin) {
        this.optimizeJoin = optimizeJoin;
        return this;
    }

    /**
     * 关闭所有优化项
     * @return
     */
    public OptimizeOptions disableAll() {
        this.optimizeJoin = false;
        this.optimizeOrderBy = false;
        return this;
    }

    /**
     * 是否所有优化项关闭
     * @return
     */
    public boolean isAllDisable(){
        return !optimizeOrderBy && !optimizeJoin;
    }

    /**
     * 是否优化Join
     *
     * @return 是否优化
     */
    public boolean isOptimizeJoin() {
        return optimizeJoin;
    }

    /**
     * 是否优化OrderBy
     *
     * @return 是否优化
     */
    public boolean isOptimizeOrderBy() {
        return optimizeOrderBy;
    }
}
