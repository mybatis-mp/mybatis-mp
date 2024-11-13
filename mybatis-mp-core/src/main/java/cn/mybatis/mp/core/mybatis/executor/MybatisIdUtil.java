package cn.mybatis.mp.core.mybatis.executor;

import java.util.regex.Pattern;

public final class MybatisIdUtil {

    private static final Pattern PATTERN = Pattern.compile("\\.");

    public static String convertIdPath(String str) {
        return PATTERN.matcher(str).replaceAll("-");
    }
}
