package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import cn.codeforfun.migrate.core.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 表结构定义
 *
 * @author wangbin
 */
@Getter
@Setter
public class Table implements Serializable, Difference {
    private static final long serialVersionUID = 411108952654575238L;

    @DbUtilProperty("TABLE_SCHEMA")
    private String schema;
    @DbUtilProperty("TABLE_NAME")
    private String name;
    @DbUtilProperty("TABLE_TYPE")
    private String type;
    @DbUtilProperty("ENGINE")
    private String engine;
    @DbUtilProperty("CREATE_TIME")
    private Timestamp createTime;
    @DbUtilProperty("UPDATE_TIME")
    private Timestamp updateTime;
    @DbUtilProperty("CHARACTER_SET_NAME")
    private String charset;
    @DbUtilProperty("TABLE_COLLATION")
    private String collate;
    @DbUtilProperty("TABLE_COMMENT")
    private String comment;

    private List<Column> columns;
    private List<Key> keys;

    private Database database;

    public static final String SQL = FileUtil.getStringByClasspath("sql/diff/create-table.sql");

    @JsonIgnore
    public String getDeleteForeignKeySql() {
        StringBuilder sb = new StringBuilder();
        for (Key key : this.keys) {
            if (key.getKeyType() == Key.KeyType.FOREIGN) {
                sb.append("ALTER TABLE `").append(key.getTableName()).append("` DROP FOREIGN KEY `").append(key.getName()).append("`;");
            }
        }
        return sb.toString();
    }

    @JsonIgnore
    @Override
    public String getUpdateSql() {
        return null;
    }

    @JsonIgnore
    @Override
    public String getDeleteSql() {
        return "DROP TABLE `" + this.name + "`;";
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        String sql = SQL;
        sql = sql.replace("${tableName}", this.name);
        sql = sql.replace("${engine}", " ENGINE = " + this.engine);
        if (this.getDatabase().getInfo().getIgnoreCharacterCompare()) {
            sql = sql.replace("${charset}", "");
            sql = sql.replace("${collate}", "");
        } else {
            sql = sql.replace("${charset}", " DEFAULT CHARSET = " + this.charset);
            sql = sql.replace("${collate}", " COLLATE = " + this.collate);
        }
        sql = sql.replace("${comment}", ObjectUtils.isEmpty(this.comment) ? "" : " COMMENT = '" + this.comment + "'");
        StringBuilder sb = new StringBuilder();
        for (Column column : this.columns) {
            String columnSql = column.getCreateTableSql();
            sb.append(columnSql);
        }
        // 唯一索引
        List<Key> uniqueIndexList = this.keys.stream().filter(s -> s.getKeyType() == Key.KeyType.UNIQUE).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(uniqueIndexList)) {
            Map<String, List<Key>> tableList = uniqueIndexList.stream().collect(Collectors.groupingBy(Key::getTableName));
            if (!ObjectUtils.isEmpty(tableList)) {
                for (Map.Entry<String, List<Key>> e : tableList.entrySet()) {
                    Map<String, List<Key>> listMap = e.getValue().stream().collect(Collectors.groupingBy(Key::getName));
                    if (!ObjectUtils.isEmpty(listMap)) {
                        for (Map.Entry<String, List<Key>> j : listMap.entrySet()) {
                            sb.append("UNIQUE KEY `").append(j.getKey()).append("`(");
                            List<String> columnList = j.getValue().stream().map(Key::getColumnName).collect(Collectors.toList());
                            if (!ObjectUtils.isEmpty(columnList)) {
                                for (String s : columnList) {
                                    sb.append("`").append(s).append("`, ");
                                }
                            }
                            sb.delete(sb.length() - 2, sb.length());
                            sb.append("),");
                        }
                    }
                }
            }
            this.keys.removeAll(uniqueIndexList);
        }
        // 主键
        List<Key> primaryKeyList = this.keys.stream().filter(k -> k.getKeyType() == Key.KeyType.PRIMARY).collect(Collectors.toList());
        if (primaryKeyList.size() > 0) {
            sb.append("PRIMARY KEY (");
            primaryKeyList.forEach(k -> sb.append("`").append(k.getColumnName()).append("`,"));
            sb.delete(sb.length() - 1, sb.length());
            sb.append("),");
        }
        this.keys.removeAll(primaryKeyList);
        for (Key key : this.keys) {
            String keySql = key.getCreateTableSql();
            sb.append(keySql);
        }
        String columnSql = sb.substring(0, sb.length() - 1);
        sql = sql.replace("${columnSql}", columnSql);
        // 索引
        final String splitStr = "#@#";
        Map<String, List<Key>> otherKeyList = this.keys.stream().filter(k -> k.getKeyType() == Key.KeyType.OTHER).collect(Collectors.groupingBy(s -> s.getTableName() + splitStr + s.getName()));
        if (!ObjectUtils.isEmpty(otherKeyList)) {
            StringBuilder sbKey = new StringBuilder();
            for (Map.Entry<String, List<Key>> m : otherKeyList.entrySet()) {
                String tableName = m.getKey().split(splitStr)[0];
                String keyName = m.getKey().split(splitStr)[1];
                List<String> columnNameList = m.getValue().stream().map(Key::getColumnName).collect(Collectors.toList());
                sbKey.append("\nCREATE INDEX `").append(keyName).append("` ON `").append(tableName).append("` (");
                for (String s : columnNameList) {
                    sbKey.append("`").append(s).append("`, ");
                }
                sbKey.setLength(sbKey.length() - 2);
                sbKey.append(");");
            }
            sql += sbKey;
        }
        return sql;
    }

    public static List<Table> configure(Connection connection, Database database) throws SQLException {
        List<Table> list1 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/table.sql"),
                Table.class, database.getInfo().getName());
        List<Column> list2 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/column.sql"),
                Column.class, database.getInfo().getName());
        List<Key> list3 = DbUtil.getBeanList(connection,
                FileUtil.getStringByClasspath("sql/detail/key.sql"),
                Key.class, database.getInfo().getName());
        return list1.stream().peek(o -> {
            o.setDatabase(database);
            o.setColumns(list2.stream().peek(s -> s.setTable(o)).filter(s -> o.getName().equals(s.getTableName())).collect(Collectors.toList()));
            o.setKeys(list3.stream().peek(s -> s.setTable(o)).filter(s -> o.getName().equals(s.getTableName())).collect(Collectors.toList()));
        }).collect(Collectors.toList());
    }

}
