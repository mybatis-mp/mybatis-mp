package db.sql.api.impl.cmd.basic;

import db.sql.api.Cmd;
import db.sql.api.DbType;
import db.sql.api.SqlBuilderContext;
import db.sql.api.impl.cmd.Methods;

/**
 * 日期格式 Pattern
 */
public interface DatePattern extends Cmd {

    DatePattern YYYY_MM_DD = new DatePattern() {
        @Override
        public String pattern(DbType dbType) {
            return String.format("%s-%s-%s",
                    yearPattern(dbType),
                    monthPattern(dbType),
                    dayPattern(dbType)
            );
        }
    };
    DatePattern YYYY_MM_DD_HH_MM_SS = new DatePattern() {
        @Override
        public String pattern(DbType dbType) {
            return String.format("%s-%s-%s %s:%s:%s",
                    yearPattern(dbType),
                    monthPattern(dbType),
                    dayPattern(dbType),
                    hourPattern(dbType),
                    minutePattern(dbType),
                    secondPattern(dbType)
            );
        }
    };

    default String yearPattern(DbType dbType) {
        switch (dbType) {
            case KING_BASE:
            case H2:
            case PGSQL:
            case ORACLE:
            case DB2: {
                return "YYYY";
            }

            case DM:
            case MARIA_DB:
            case MYSQL: {
                return "%Y";
            }

            case SQL_SERVER: {
                return "yyyy";
            }
            default: {
                throw new RuntimeException("Not supported");
            }
        }
    }

    default String monthPattern(DbType dbType) {
        switch (dbType) {
            case KING_BASE:
            case H2:
            case SQL_SERVER:
            case PGSQL:
            case ORACLE:
            case DB2: {
                return "MM";
            }

            case DM:
            case MARIA_DB:
            case MYSQL: {
                return "%m";
            }

            default: {
                throw new RuntimeException("Not supported");
            }
        }
    }

    default String dayPattern(DbType dbType) {
        switch (dbType) {
            case KING_BASE:
            case H2:
            case PGSQL:
            case ORACLE:
            case DB2: {
                return "DD";
            }

            case DM:
            case MARIA_DB:
            case MYSQL: {
                return "%d";
            }

            case SQL_SERVER: {
                return "dd";
            }
            default: {
                throw new RuntimeException("Not supported");
            }
        }
    }

    default String hourPattern(DbType dbType) {
        switch (dbType) {
            case KING_BASE:
            case H2:
            case PGSQL:
            case ORACLE:
            case DB2: {
                return "HH24";
            }

            case DM:
            case MARIA_DB:
            case MYSQL: {
                return "%H";
            }

            case SQL_SERVER: {
                return "HH";
            }
            default: {
                throw new RuntimeException("Not supported");
            }
        }
    }

    default String minutePattern(DbType dbType) {
        switch (dbType) {
            case KING_BASE:
            case H2:
            case PGSQL:
            case ORACLE:
            case DB2: {
                return "MI";
            }

            case DM:
            case MARIA_DB:
            case MYSQL: {
                return "%i";
            }

            case SQL_SERVER: {
                return "mm";
            }
            default: {
                throw new RuntimeException("Not supported");
            }
        }
    }

    default String secondPattern(DbType dbType) {
        switch (dbType) {
            case KING_BASE:
            case H2:
            case PGSQL:
            case ORACLE:
            case DB2: {
                return "SS";
            }

            case DM:
            case MARIA_DB:
            case MYSQL: {
                return "%s";
            }

            case SQL_SERVER: {
                return "ss";
            }
            default: {
                throw new RuntimeException("Not supported");
            }
        }
    }

    String pattern(DbType dbType);

    @Override
    default StringBuilder sql(Cmd module, Cmd parent, SqlBuilderContext context, StringBuilder sqlBuilder) {
        Cmd pattern = Methods.convert(pattern(context.getDbType()));
        pattern.sql(module, parent, context, sqlBuilder);
        return sqlBuilder;
    }

    @Override
    default boolean contain(Cmd cmd) {
        return false;
    }
}
