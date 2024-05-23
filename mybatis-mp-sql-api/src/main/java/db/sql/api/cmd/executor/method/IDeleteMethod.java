package db.sql.api.cmd.executor.method;

public interface IDeleteMethod<SELF extends IDeleteMethod, DATASET> {


    SELF delete(DATASET... tables);

    SELF delete(Class<?>... entities);
}
