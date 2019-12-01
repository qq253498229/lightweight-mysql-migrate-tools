package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.utils.DbUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 表定义
 *
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class TableStructure implements Serializable {
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


    public static List<TableStructure> configure(Connection connection, String databaseName) throws IOException, SQLException {
        List<TableStructure> result = new ArrayList<>();
        List<Map<String, Object>> structure = DbUtil.getTableStructure(connection, databaseName);
        //todo
        return result;
    }
}
