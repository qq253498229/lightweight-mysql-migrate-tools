package cn.codeforfun.core.entity.structure;

import cn.codeforfun.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wangbin
 */
@Getter
@Setter
public class Column {
    private String name;
    private String type;
    private Integer length;
    private String defaultValue;
    private String comment;

    private Boolean notNull;
    private Boolean autoIncrement;
    private Boolean unique;
    private Boolean primaryKey;


    private static final Pattern PATTERN_ALL = Pattern.compile("(?<name>\\S+)\\s*(?<type>\\w+)+(?:\\((?<length>\\d+)\\))?\\s*(?<notNull>not null)?\\s*(?:default\\s+(?<default>\\w+))?\\s*(?<autoIncrement>(?:auto_increment)+)?\\s*(?:comment\\s+(?<comment>\\S+))?.*(?:,|$)", Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_KEY = Pattern.compile("key(?!`)(?<!`)", Pattern.CASE_INSENSITIVE);

    public static List<Column> configure(String sql) {
        List<Column> columnList = new ArrayList<>();
        Matcher matcher = PATTERN_ALL.matcher(sql);
        while (matcher.find()) {
            String columnSql = matcher.group(0);
            if (PATTERN_KEY.matcher(columnSql).find()) {
                continue;
            }
            Column column = new Column();
            column.setName(StringUtil.deleteDot(matcher.group("name")));
            column.setType(matcher.group("type"));
            Integer length = ObjectUtils.isEmpty(matcher.group("length")) ? null : Integer.valueOf(matcher.group("length"));
            column.setLength(length);
            column.setDefaultValue(matcher.group("default"));
            column.setComment(StringUtil.deleteDot(matcher.group("comment")));
            column.setNotNull(!ObjectUtils.isEmpty(matcher.group("notNull")));
            column.setAutoIncrement(!ObjectUtils.isEmpty(matcher.group("autoIncrement")));
            columnList.add(column);
        }
        return columnList;
    }
}
