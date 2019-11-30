package cn.codeforfun.core.entity.structure;

import cn.codeforfun.core.entity.structure.utils.ConvertUtil;
import cn.codeforfun.utils.DbUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * 表定义
 *
 * @author wangbin
 */
@Getter
@Setter
public class Table implements Serializable {
    private static final long serialVersionUID = 411108952654575238L;
    private String name;
    private String comment;
    private String engine;
    private String charset;
    private String collate;
    private List<Column> columns;
    private List<Key> keys;
    private List<Index> indices;
    private List<ForeignKey> foreignKeys;

    public static List<Table> configure(Connection connection) {
        List<String> tableNameList = DbUtil.executeSql(connection, "show tables");
        if (ObjectUtils.isEmpty(tableNameList)) {
            return new ArrayList<>();
        }
        List<Table> tableList = new ArrayList<>();
        for (String tableName : tableNameList) {
            String tableStructure = DbUtil.executeSql(connection, "show create table " + tableName).get(0);
            Table table = new Table();
            table.setName(tableName);
            table.setEngine(ConvertUtil.tableEngine(tableStructure));
            table.setCharset(ConvertUtil.tableCharset(tableStructure));
            table.setCollate(ConvertUtil.tableCollate(tableStructure));
            table.setComment(ConvertUtil.tableComment(tableStructure));
            table.setColumns(Column.configure(tableStructure));
            tableList.add(table);
        }
        return tableList;
    }


}
