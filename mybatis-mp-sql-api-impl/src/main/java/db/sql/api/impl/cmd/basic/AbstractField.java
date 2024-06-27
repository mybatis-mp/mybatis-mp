package db.sql.api.impl.cmd.basic;

import db.sql.api.cmd.basic.IField;
import db.sql.api.impl.cmd.dbFun.FunctionInterface;

public abstract class AbstractField<T extends AbstractField<T>> extends AbstractAlias<T> implements Value, IField<T>, FunctionInterface {


}
