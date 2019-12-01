package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 表定义
 *
 * @author wangbin
 */
@Getter
@Setter
public class Table implements Serializable {
    private static final long serialVersionUID = 411108952654575238L;

    public static final String TABLE_REGEX = "\\s*create\\s+table\\s+(?<name>\\S+)[^(]+\\((?<columnSql>[\\s\\S]+)\\)\\s*(?:engine=(?<engine>\\S+))?\\s*(?:default charset=(?<charset>\\S+))?\\s*(?:collate=(?<collate>\\S+))?\\s*(?:comment=(?<comment>\\S+))?";

    private String name;
    private String comment;
    private String engine;
    private String charset;
    private String collate;
    private List<Column> columns;
    private List<Key> keys;
    private List<Index> indices;
    private List<ForeignKey> foreignKeys;

    public static List<Table> configTableStructure(Connection connection) {
        List<String> tableNameList = DbUtil.executeSql(connection, "show tables");
        if (ObjectUtils.isEmpty(tableNameList)) {
            return new ArrayList<>();
        }
        List<Table> tableList = new ArrayList<>();
        for (String tableName : tableNameList) {
            String structureSql = DbUtil.executeSql(connection, "show create table " + tableName).get(0);
            Pattern pattern = Pattern.compile(TABLE_REGEX, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(structureSql);
            if (matcher.find()) {
                Table table = new Table();
                table.setName(StringUtil.deleteDot(matcher.group("name")));
                table.setEngine(matcher.group("engine"));
                table.setCharset(matcher.group("charset"));
                table.setCollate(matcher.group("collate"));
                table.setComment(StringUtil.deleteDot(matcher.group("comment")));
                table.setColumns(Column.configure(matcher.group("columnSql")));
                tableList.add(table);
            }

        }
        return tableList;
    }


}
