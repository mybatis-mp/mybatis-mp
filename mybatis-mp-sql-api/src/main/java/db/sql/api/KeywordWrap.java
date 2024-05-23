package db.sql.api;

public class KeywordWrap {

    private final String prefix;
    private final String suffix;

    private final boolean toUpperCase;

    public KeywordWrap(String prefix, String suffix) {
        this(prefix, suffix, false);
    }

    public KeywordWrap(String prefix, String suffix, boolean toUpperCase) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.toUpperCase = toUpperCase;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isToUpperCase() {
        return toUpperCase;
    }
}
