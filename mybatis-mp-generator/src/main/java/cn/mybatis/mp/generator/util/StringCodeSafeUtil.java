package cn.mybatis.mp.generator.util;

public final class  StringCodeSafeUtil {

    public static String removeSpecialCharacters(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }
        return str.replaceAll("\r|\n|\t","；");
    }

    public static void main(String[] args) {
        System.out.println(removeSpecialCharacters("a   a"));
        System.out.println(removeSpecialCharacters("安全\n" +
                "[ān quán]"));

    }
}
