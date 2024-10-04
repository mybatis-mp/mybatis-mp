package db.sql.api;

import java.io.Serializable;
import java.util.function.Function;


public interface GetterFun<T, R> extends Function<T, R>, Serializable {


}
