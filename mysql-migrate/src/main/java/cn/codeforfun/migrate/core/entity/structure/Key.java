package cn.codeforfun.migrate.core.entity.structure;

import cn.codeforfun.migrate.core.diff.Difference;
import cn.codeforfun.migrate.core.entity.structure.annotations.DbUtilProperty;
import cn.codeforfun.migrate.core.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Key结构定义
 *
 * @author wangbin
 */
@Getter
@Setter
public class Key implements Difference, Serializable {
    private static final long serialVersionUID = 5489127924529636485L;

    public static final String FLAG_PRIMARY = "PRIMARY";

    @DbUtilProperty("CONSTRAINT_SCHEMA")
    private String schema;
    @DbUtilProperty("CONSTRAINT_NAME")
    private String name;
    @DbUtilProperty("TABLE_NAME")
    private String tableName;
    @DbUtilProperty("COLUMN_NAME")
    private String columnName;
    @DbUtilProperty("ORDINAL_POSITION")
    private Long ordinalPosition;
    @DbUtilProperty("POSITION_IN_UNIQUE_CONSTRAINT")
    private Long positionInUniqueConstraint;
    @DbUtilProperty("REFERENCED_TABLE_SCHEMA")
    private String referencedSchema;
    @DbUtilProperty("REFERENCED_TABLE_NAME")
    private String referencedTable;
    @DbUtilProperty("REFERENCED_COLUMN_NAME")
    private String referencedColumn;

    private Table table;

    public enum KeyType {
        PRIMARY, FOREIGN, UNIQUE
    }

    public KeyType getKeyType() {
        if ("PRIMARY".equals(this.getName())) {
            return KeyType.PRIMARY;
        } else if (this.getReferencedSchema() != null || this.getReferencedTable() != null || this.getReferencedColumn() != null) {
            return KeyType.FOREIGN;
        } else {
            return KeyType.UNIQUE;
        }
    }

    public static void resolveDeleteSql(List<Difference> delete, List<String> sqlList) {
        List<Key> deleteKeyList = delete.stream().filter(s -> s instanceof Key).map(s -> (Key) s).collect(Collectors.toList());

        List<Key> uniqueKeyList = deleteKeyList.stream().filter(s -> !FLAG_PRIMARY.equals(s.getName()) && ObjectUtils.isEmpty(s.getReferencedColumn())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(uniqueKeyList)) {
            Map<String, List<Key>> collect = uniqueKeyList.stream().collect(Collectors.groupingBy(Key::getTableName));
            for (Map.Entry<String, List<Key>> entry : collect.entrySet()) {
                Map<String, List<Key>> collect1 = entry.getValue().stream().collect(Collectors.groupingBy(Key::getName));
                for (Map.Entry<String, List<Key>> stringListEntry : collect1.entrySet()) {
                    sqlList.add("ALTER TABLE `" + entry.getKey() + "` DROP KEY `" + stringListEntry.getKey() + "`;");
                }
            }
        }
        List<Key> otherKeyList = deleteKeyList.stream().filter(s -> FLAG_PRIMARY.equals(s.getName()) || !ObjectUtils.isEmpty(s.getReferencedColumn())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(otherKeyList)) {
            for (Key key : otherKeyList) {
                sqlList.add(key.getDeleteSql());
            }
        }
    }

    public static void resolveCreateSql(List<Difference> create, List<String> sqlList) {
        if (ObjectUtils.isEmpty(create)) {
            return;
        }
        List<Key> keyList = create.stream().filter(s -> s instanceof Key).map(s -> (Key) s).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(keyList)) {
            return;
        }
        List<Key> uniqueKeyList = keyList.stream().filter(s -> !FLAG_PRIMARY.equals(s.getName()) && ObjectUtils.isEmpty(s.getReferencedColumn())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(uniqueKeyList)) {
            Map<String, List<Key>> tableList = uniqueKeyList.stream().collect(Collectors.groupingBy(Key::getTableName));
            if (!ObjectUtils.isEmpty(tableList)) {
                for (Map.Entry<String, List<Key>> e : tableList.entrySet()) {
                    Map<String, List<Key>> listMap = e.getValue().stream().collect(Collectors.groupingBy(Key::getName));
                    if (ObjectUtils.isEmpty(listMap)) {
                        continue;
                    }
                    for (Map.Entry<String, List<Key>> j : listMap.entrySet()) {
                        List<String> columnList = j.getValue().stream().map(Key::getColumnName).collect(Collectors.toList());
                        if (ObjectUtils.isEmpty(columnList)) {
                            continue;
                        }
                        StringBuilder sb = new StringBuilder();
                        sb.append("ALTER TABLE `");
                        sb.append(e.getKey());
                        sb.append("` ADD CONSTRAINT `");
                        sb.append(j.getKey());
                        sb.append("` UNIQUE (");
                        for (String s : columnList) {
                            sb.append("`").append(s).append("`").append(", ");
                        }
                        sb.setLength(sb.length() - 2);
                        sb.append(");");
                        sqlList.add(sb.toString());
                    }
                }
            }
        }
        List<Key> otherKeyList = keyList.stream().filter(s -> FLAG_PRIMARY.equals(s.getName()) || !ObjectUtils.isEmpty(s.getReferencedColumn())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(otherKeyList)) {
            for (Key key : otherKeyList) {
                sqlList.add(key.getCreateSql());
            }
        }
    }

    public static void resolveUpdateSql(List<Difference> update, List<String> sqlList) {
        if (ObjectUtils.isEmpty(update)) {
            return;
        }
        List<Key> keyList = update.stream().filter(s -> s instanceof Key).map(s -> (Key) s).collect(Collectors.toList());
        if (ObjectUtils.isEmpty(keyList)) {
            return;
        }
        List<Key> uniqueKeyList = keyList.stream().filter(s -> !FLAG_PRIMARY.equals(s.getName()) && ObjectUtils.isEmpty(s.getReferencedColumn())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(uniqueKeyList)) {
            Map<String, List<Key>> tableList = uniqueKeyList.stream().collect(Collectors.groupingBy(Key::getTableName));
            if (!ObjectUtils.isEmpty(tableList)) {
                for (Map.Entry<String, List<Key>> e : tableList.entrySet()) {
                    Map<String, List<Key>> listMap = e.getValue().stream().collect(Collectors.groupingBy(Key::getName));
                    if (ObjectUtils.isEmpty(listMap)) {
                        continue;
                    }
                    for (Map.Entry<String, List<Key>> j : listMap.entrySet()) {
                        List<String> columnList = j.getValue().stream().map(Key::getColumnName).collect(Collectors.toList());
                        if (ObjectUtils.isEmpty(columnList)) {
                            continue;
                        }
                        sqlList.add("ALTER TABLE `" + e.getKey() + "` DROP KEY `" + j.getKey() + "`;");
                    }
                }
            }
            resolveCreateSql(update, sqlList);
        }
        List<Key> otherKeyList = keyList.stream().filter(s -> FLAG_PRIMARY.equals(s.getName()) || !ObjectUtils.isEmpty(s.getReferencedColumn())).collect(Collectors.toList());
        if (!ObjectUtils.isEmpty(otherKeyList)) {
            for (Key key : otherKeyList) {
                sqlList.add(key.getDeleteSql());
                sqlList.add(key.getCreateSql());
            }
        }
    }

    @JsonIgnore
    @Override
    public String getDeleteSql() {
        StringBuilder sb = new StringBuilder();
        if (FLAG_PRIMARY.equals(this.name)) {
            // 主键
            sb.append("ALTER TABLE `").append(this.tableName).append("` DROP PRIMARY KEY;");
        } else if (ObjectUtils.isEmpty(this.referencedSchema)
                && ObjectUtils.isEmpty(this.referencedTable)
                && ObjectUtils.isEmpty(this.referencedColumn)) {
            // 唯一索引
            sb.append("ALTER TABLE `").append(this.tableName).append("` DROP KEY `").append(this.name).append("`;");
        } else {
            // 外键
            sb.append("ALTER TABLE `").append(this.tableName).append("` DROP FOREIGN KEY `").append(this.name).append("`;");
        }
        return sb.toString();
    }

    @JsonIgnore
    public String getCreateTableSql() {
        //获取key sql
        StringBuilder sb = new StringBuilder();
        if (ObjectUtils.isEmpty(this.referencedSchema)
                && ObjectUtils.isEmpty(this.referencedTable)
                && ObjectUtils.isEmpty(this.referencedColumn)) {
            // 唯一索引
            sb.append("CONSTRAINT `").append(this.name).append("` UNIQUE (`").append(this.columnName).append("`),");
        } else {
            // 外键
            sb.append("CONSTRAINT `").append(this.name).append("` ")
                    .append("FOREIGN KEY (`").append(this.columnName).append("`) ")
                    .append("REFERENCES `").append(this.referencedTable).append("` ")
                    .append("(`").append(this.referencedColumn).append("`),");
        }
        return sb.toString();
    }

    @JsonIgnore
    @Override
    public String getCreateSql() {
        StringBuilder sb = new StringBuilder();
        if (FLAG_PRIMARY.equals(this.name)) {
            // 主键
            sb.append("ALTER TABLE `").append(this.tableName).append("` ADD PRIMARY KEY (`").append(this.columnName).append("`);");
        } else if (ObjectUtils.isEmpty(this.referencedSchema)
                && ObjectUtils.isEmpty(this.referencedTable)
                && ObjectUtils.isEmpty(this.referencedColumn)) {
            // 唯一索引
            sb.append("ALTER TABLE `").append(this.tableName)
                    .append("` ADD CONSTRAINT `").append(this.name).append("` UNIQUE (`").append(this.columnName).append("`);");
        } else {
            // 外键
            sb.append("ALTER TABLE `").append(this.tableName).append("` ADD KEY `").append(this.name).append("` (`").append(this.columnName).append("`); ");
            sb.append("ALTER TABLE `").append(this.tableName).append("` ADD CONSTRAINT `").append(this.name).append("` ")
                    .append("FOREIGN KEY (`").append(this.columnName).append("`) ")
                    .append("REFERENCES `").append(this.referencedTable).append("` ")
                    .append("(`").append(this.referencedColumn).append("`);");
        }
        return sb.toString();
    }

    @JsonIgnore
    @Override
    public String getUpdateSql() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Key)) {
            return false;
        }
        Key key = (Key) o;
        // todo
        if (getKeyType() == KeyType.UNIQUE && key.getKeyType() == KeyType.UNIQUE) {
            return Objects.equals(getName(), key.getName()) &&
                    Objects.equals(getTableName(), key.getTableName()) &&
                    Objects.equals(getColumnName(), key.getColumnName()) &&
                    Objects.equals(getPositionInUniqueConstraint(), key.getPositionInUniqueConstraint()) &&
                    Objects.equals(getReferencedSchema(), key.getReferencedSchema()) &&
                    Objects.equals(getReferencedTable(), key.getReferencedTable()) &&
                    Objects.equals(getReferencedColumn(), key.getReferencedColumn());
        }
        return Objects.equals(getName(), key.getName()) &&
                Objects.equals(getTableName(), key.getTableName()) &&
                Objects.equals(getColumnName(), key.getColumnName()) &&
                Objects.equals(getOrdinalPosition(), key.getOrdinalPosition()) &&
                Objects.equals(getPositionInUniqueConstraint(), key.getPositionInUniqueConstraint()) &&
                Objects.equals(getReferencedSchema(), key.getReferencedSchema()) &&
                Objects.equals(getReferencedTable(), key.getReferencedTable()) &&
                Objects.equals(getReferencedColumn(), key.getReferencedColumn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getTableName(), getColumnName(), getOrdinalPosition(), getPositionInUniqueConstraint(), getReferencedSchema(), getReferencedTable(), getReferencedColumn());
    }
}
