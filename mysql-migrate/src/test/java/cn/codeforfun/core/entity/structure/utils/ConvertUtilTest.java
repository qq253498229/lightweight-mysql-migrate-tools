package cn.codeforfun.core.entity.structure.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
class ConvertUtilTest {
    public static final String DATABASE_STRUCTURE = "CREATE DATABASE `test_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */";
    public static final String TABLE_STRUCTURE = "CREATE TABLE `test_table` (\n" +
            "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
            "  `test_column_1` int(11) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试表备注'";

    @Test
    void databaseCharacter() {
        String result = ConvertUtil.databaseCharacter(DATABASE_STRUCTURE);
        assertEquals(result, "utf8mb4");
    }

    @Test
    void databaseCollate() {
        String result = ConvertUtil.databaseCollate(DATABASE_STRUCTURE);
        assertEquals(result, "utf8mb4_unicode_ci");
    }

    @Test
    void tableCharset() {
        String result = ConvertUtil.tableCharset(TABLE_STRUCTURE);
        assertEquals(result, "utf8mb4");
    }

    @Test
    void tableCollate() {
        String result = ConvertUtil.tableCollate(TABLE_STRUCTURE);
        assertEquals(result, "utf8mb4_unicode_ci");
    }

    @Test
    void tableEngine() {
        String result = ConvertUtil.tableEngine(TABLE_STRUCTURE);
        assertEquals(result, "InnoDB");
    }

    @Test
    void tableComment() {
        String result = ConvertUtil.tableComment(TABLE_STRUCTURE);
        assertEquals(result, "测试表备注");
    }

    @Test
    public void test1() {
        String sql = "CREATE TABLE `test_table` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `test_column` int(11) DEFAULT NULL COMMENT '测试字段备注',\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='测试表备注'";
        String regex = "^\\s*create\\s+table\\s+(?<tableName>\\S+)?[^()]*\\(" +
                "(?<columnSql>[\\s\\S]+)" +
                "\\)[\\s]+" +
                "(engine=(?<tableEngine>\\S+))?" +
                "[\\s]*" +
                "(default charset=(?<tableCharset>\\S+))?" +
                "[\\s]*" +
                "(collate=(?<tableCollate>\\S+))?" +
                "[\\s]*" +
                "(comment=(?<tableComment>\\S+))?" +
                "$";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(sql);
        if (matcher.find()) {
            System.out.println("tableName:" + matcher.group("tableName"));
            System.out.println("columnSql:" + matcher.group("columnSql"));
            System.out.println("tableEngine:" + matcher.group("tableEngine"));
            System.out.println("tableCharset:" + matcher.group("tableCharset"));
            System.out.println("tableCollate:" + matcher.group("tableCollate"));
            System.out.println("tableComment:" + matcher.group("tableComment"));
        }

    }


    //匹配整个ddl，将ddl分为表名，列sql部分，表注释
    private static final Pattern DDL_PATTERN = Pattern.compile("\\s*create\\s+table\\s+(?<tableName>\\S+)[^(]*\\((?<columnsSQL>[\\s\\S]+)\\)[^)]+?(comment\\s*(=|on\\s+table)\\s*'(?<tableComment>.*?)'\\s*;?)?$", Pattern.CASE_INSENSITIVE);
    //匹配列sql部分，分别解析每一列的列名 类型 和列注释
    private static final Pattern COL_PATTERN = Pattern.compile("\\s*`(?<fieldName>\\S+)`\\s+(?<fieldType>\\w+)\\s*(?:\\([\\s\\d,]+\\))?((?!comment).)*(comment\\s*'(?<fieldComment>.*?)')?\\s*(,|$)", Pattern.CASE_INSENSITIVE);

    @Test
    public void parse() {
        Matcher matcher = DDL_PATTERN.matcher(TABLE_STRUCTURE);
        if (matcher.find()) {
            String tableName = matcher.group("tableName");
            String tableComment = matcher.group("tableComment");
            System.out.println(tableName + "\t\t" + tableComment);
            System.out.println("==========");
            String columnsSQL = matcher.group("columnsSQL");
            if (columnsSQL != null && columnsSQL.length() > 0) {
                Matcher colMatcher = COL_PATTERN.matcher(columnsSQL);
                while (colMatcher.find()) {
                    String fieldName = colMatcher.group("fieldName");
                    String fieldType = colMatcher.group("fieldType");
                    String fieldComment = colMatcher.group("fieldComment");
                    System.out.println(fieldName + "\t\t" + fieldType + "\t\t" + fieldComment);
                }
            }
        }
    }
}