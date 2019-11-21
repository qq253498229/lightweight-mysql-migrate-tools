package cn.codeforfun.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangbin
 */
public class StringUtil {
    public static List<String> getListByPattern(String str, String prefix, String suffix) {
        return getList(str, prefix, suffix);
    }

    public static String getStringByPattern(String str, String prefix, String suffix) {
        List<String> listMatches = getList(str, prefix, suffix);
        if (listMatches.size() == 1) {
            return listMatches.get(0);
        } else if (listMatches.size() == 0) {
            return null;
        }
        throw new RuntimeException("匹配失败");
    }

    private static List<String> getList(String str, String prefix, String suffix) {
        Pattern pattern = Pattern.compile("(" + prefix + ")(.*?)(" + suffix + ")");
        Matcher matcher = pattern.matcher(str);
        List<String> listMatches = new ArrayList<>();
        while (matcher.find()) {
            listMatches.add(matcher.group(2));
        }
        return listMatches;
    }
}
