package cn.codeforfun.migrate.core.utils;

import org.springframework.util.ObjectUtils;

/**
 * @author wangbin
 */
public class StringUtil {

    public static final String DOT_1 = "`";
    public static final String DOT_2 = "'";

    public static String deleteDot(String input) {
        if (ObjectUtils.isEmpty(input)) {
            return input;
        }
        if (input.startsWith(DOT_1) && input.endsWith(DOT_1)) {
            input = input.substring(1, input.length() - 1);
        }
        if (input.startsWith(DOT_2) && input.endsWith(DOT_2)) {
            input = input.substring(1, input.length() - 1);
        }
        return input;
    }
}
