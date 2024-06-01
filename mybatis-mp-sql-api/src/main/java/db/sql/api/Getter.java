package db.sql.api;

import java.io.Serializable;
import java.util.function.Function;


public interface Getter<T> extends Function<T, Object>, Serializable {


}
