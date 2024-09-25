package db.sql.api.impl.tookit;

import java.util.Locale;

public class PropertyNamer {
    private PropertyNamer() {
    }

    public static String methodToProperty(String name) {
        if (name.startsWith("is")) {
            name = name.substring(2);
        } else {
            if (!name.startsWith("get") && !name.startsWith("set")) {
                throw new RuntimeException("Error parsing property name '" + name + "'.  Didn't start with 'is', 'get' or 'set'.");
            }

            name = name.substring(3);
        }

        if (name.length() == 1 || name.length() > 1 && !Character.isUpperCase(name.charAt(1))) {
            name = name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
        }

        return name;
    }

    public static boolean isProperty(String name) {
        return isGetter(name) || isSetter(name);
    }

    public static boolean isGetter(String name) {
        return name.startsWith("get") && name.length() > 3 || name.startsWith("is") && name.length() > 2;
    }

    public static boolean isSetter(String name) {
        return name.startsWith("set") && name.length() > 3;
    }


    /**
     * 将驼峰命名转换为下划线命名
     *
     * @param str 驼峰命名的字符串
     * @return 转换后的下划线命名的字符串
     */
    public static String camelToUnderscore(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }

        StringBuilder result = new StringBuilder(str.length() * 2);
        int i = 0;
        char[] chars = str.toCharArray();
        for (char c : chars) {
            if (i > 0 && Character.isUpperCase(c)) {
                result.append('_');
            }
            result.append(Character.toLowerCase(c));
            i++;
        }

        return result.toString();
    }


}
