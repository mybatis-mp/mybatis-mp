package db.sql.api;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public enum DbType {

    H2(new KeywordWrap("`", "`"), new HashSet<>()),

    MYSQL(new KeywordWrap("`", "`"), new HashSet<>()),

    MARIA_DB(new KeywordWrap("`", "`"), new HashSet<>()),

    SQL_SERVER(new KeywordWrap("[", "]"), new HashSet<>()),

    PGSQL(new KeywordWrap("\"", "\""), new HashSet<>()),

    ORACLE(new KeywordWrap("\"", "\"", true), new HashSet<>()),

    DM(new KeywordWrap("\"", "\"", true), new HashSet<>()),

    DB2(new KeywordWrap("\"", "\"", true), new HashSet<>()),

    KING_BASE(new KeywordWrap("\"", "\"", true), new HashSet<>()),

    CLICK_HOUSE(new KeywordWrap("\"", "\"", true), new HashSet<>());

    private final KeywordWrap keywordWrap;
    private final Set<String> keywords;

    DbType(KeywordWrap keywordWrap, Set<String> keywords) {
        this.keywordWrap = keywordWrap;
        this.keywords = keywords;
    }

    public static DbType getByName(String name) {
        DbType[] dbTypes = values();
        for (DbType dbType : dbTypes) {
            if (dbType.name().equals(name)) {
                return dbType;
            }
        }
        return MYSQL;
    }

    public KeywordWrap getKeywordWrap() {
        return keywordWrap;
    }

    public Set<String> getKeywords() {
        return keywords;
    }

    public String wrap(String name) {
        if (getKeywords().contains(name)) {
            if (getKeywordWrap().isToUpperCase()) {
                name = name.toUpperCase(Locale.ENGLISH);
            }
            return String.format("%s%s%s", getKeywordWrap().getPrefix(), name, getKeywordWrap().getSuffix());
        }
        return name;
    }
}
