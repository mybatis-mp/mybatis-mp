package db.sql.api.cmd.executor.method;

public interface IForUpdateMethod<SELF> {

    default SELF forUpdate() {
        return forUpdate(true);
    }

    SELF forUpdate(boolean wait);

}
