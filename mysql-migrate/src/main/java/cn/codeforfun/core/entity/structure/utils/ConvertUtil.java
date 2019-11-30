package cn.codeforfun.core.entity.structure.utils;

import cn.codeforfun.utils.StringUtil;

/**
 * @author wangbin
 */
public class ConvertUtil {

    public static String databaseCharacter(String databaseStructure) {
        return StringUtil.getStringByPattern(databaseStructure, "CHARACTER SET ", " ");
    }

    public static String databaseCollate(String databaseStructure) {
        return StringUtil.getStringByPattern(databaseStructure, "COLLATE ", " ");
    }

    public static String tableCharset(String tableStructure) {
        return StringUtil.getStringByPattern(tableStructure, "CHARSET=", " ");
    }

    public static String tableCollate(String tableStructure) {
        return StringUtil.getStringByPattern(tableStructure, "COLLATE=", " ");
    }

    public static String tableEngine(String tableStructure) {
        return StringUtil.getStringByPattern(tableStructure, "ENGINE=", " ");
    }

    public static String tableComment(String tableStructure) {
        return StringUtil.getStringByPattern(tableStructure, "COMMENT='", "'");
    }

    public static String tableColumns(String tableStructure) {
        return StringUtil.getStringByPattern(tableStructure, "\\(\n", "\n\\)");
    }

}
