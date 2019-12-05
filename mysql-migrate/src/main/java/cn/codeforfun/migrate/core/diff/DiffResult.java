package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.Column;
import cn.codeforfun.migrate.core.entity.structure.Database;
import cn.codeforfun.migrate.core.entity.structure.Key;
import cn.codeforfun.migrate.core.entity.structure.Table;
import cn.codeforfun.migrate.core.utils.DbUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class DiffResult {

    private Database from;
    private Database to;

    private List<Difference> delete = new ArrayList<>();
    private List<Difference> create = new ArrayList<>();
    private List<Difference> update = new ArrayList<>();

    public DiffResult(Database from, Database to) {
        this.from = from;
        this.to = to;
    }

    public DiffResult compare() {
        List<Table> fromTableList = this.from.getTables();
        List<Table> toTableList = this.to.getTables();
        List<String> fromTableNameList = fromTableList.stream().map(Table::getName).collect(Collectors.toList());
        List<String> toTableNameList = toTableList.stream().map(Table::getName).collect(Collectors.toList());
        // 删除表
        List<Table> deleteTableList = toTableList.stream().filter(s -> !fromTableNameList.contains(s.getName())).collect(Collectors.toList());
        this.delete.addAll(deleteTableList);
        // 新建表
        List<Table> createTableList = fromTableList.stream().filter(s -> !toTableNameList.contains(s.getName())).collect(Collectors.toList());
        this.create.addAll(createTableList);
        // 需要更新的表
        List<Table> fromUpdateTableList = fromTableList.stream().filter(s -> toTableNameList.contains(s.getName())).collect(Collectors.toList());
        List<Table> toUpdateTableList = toTableList.stream().filter(s -> fromTableNameList.contains(s.getName())).collect(Collectors.toList());

        // 更新key
        List<Key> fromKeyList = fromUpdateTableList.stream().map(Table::getKeys).flatMap(List::stream).collect(Collectors.toList());
        List<Key> toKeyList = toUpdateTableList.stream().map(Table::getKeys).flatMap(List::stream).collect(Collectors.toList());
        List<String> fromKeyNameList = fromKeyList.stream().map(Key::getName).collect(Collectors.toList());
        List<String> toKeyNameList = toKeyList.stream().map(Key::getName).collect(Collectors.toList());
        // 删除key
        List<Key> deleteKeyList = toKeyList.stream().filter(s -> !fromKeyNameList.contains(s.getName())).collect(Collectors.toList());
        this.delete.addAll(deleteKeyList);
        // 新建key
        List<Key> createKeyList = fromKeyList.stream().filter(s -> !toKeyNameList.contains(s.getName())).collect(Collectors.toList());
        this.create.addAll(createKeyList);
        // 更新key
        List<Key> updateKeyList = new ArrayList<>();
        for (Key fromKey : fromKeyList) {
            for (Key toKey : toKeyList) {
                if (fromKey.getName().equals(toKey.getName()) && !fromKey.equals(toKey)) {
                    updateKeyList.add(fromKey);
                }
            }
        }
        this.update.addAll(updateKeyList);

        // 更新字段
        List<Column> fromColumnList = fromUpdateTableList.stream().map(Table::getColumns).flatMap(List::stream).collect(Collectors.toList());
        List<Column> toColumnList = toUpdateTableList.stream().map(Table::getColumns).flatMap(List::stream).collect(Collectors.toList());
        List<String> fromColumnNameList = fromColumnList.stream().map(Column::getName).collect(Collectors.toList());
        List<String> toColumnNameList = toColumnList.stream().map(Column::getName).collect(Collectors.toList());
        // 删除字段
        List<Column> deleteColumnList = toColumnList.stream().filter(s -> !fromColumnNameList.contains(s.getName())).collect(Collectors.toList());
        this.delete.addAll(deleteColumnList);
        // 新建字段
        List<Column> createColumnList = fromColumnList.stream().filter(s -> !toColumnNameList.contains(s.getName())).collect(Collectors.toList());
        this.create.addAll(createColumnList);
        // 更新字段
        List<Column> updateColumnList = new ArrayList<>();
        for (Column fromColumn : fromColumnList) {
            for (Column toColumn : toColumnList) {
                if (fromColumn.getName().equals(toColumn.getName()) && !fromColumn.equals(toColumn)) {
                    updateColumnList.add(fromColumn);
                }
            }
        }
        this.update.addAll(updateColumnList);
        return this;
    }

    private void resolveDeleteSql(StringBuilder sb) {
        for (Difference difference : this.delete) {
            if (difference instanceof Table) {
                // 先删除外键
                Table delete = (Table) difference;
                String deleteForeignKeySql = delete.getDeleteForeignKeySql();
                sb.append(deleteForeignKeySql);
                // 再删除表
                String deleteSql = delete.getDeleteSql();
                sb.append(deleteSql).append("\n");
            } else if (difference instanceof Key) {
                // 删除key
                Key delete = (Key) difference;
                String deleteSql = delete.getDeleteSql();
                sb.append(deleteSql);
            } else if (difference instanceof Column) {
                // 删除字段
                Column delete = (Column) difference;
                String deleteSql = delete.getDeleteSql();
                sb.append(deleteSql);
            }
        }
    }

    private void resolveCreateSql(StringBuilder sb) {
        for (Difference difference : this.create) {
            if (difference instanceof Table) {
                // 创建表
                Table table = (Table) difference;
                String createSql = table.getCreateSql();
                sb.append(createSql).append("\n");
            } else if (difference instanceof Key) {
                // 创建key
                Key delete = (Key) difference;
                String createSql = delete.getCreateSql();
                sb.append(createSql);
            } else if (difference instanceof Column) {
                // 创建字段
                Column delete = (Column) difference;
                String createSql = delete.getCreateSql();
                sb.append(createSql);
            }
        }
    }

    private void resolveUpdateSql(StringBuilder sb) {
        for (Difference difference : this.update) {
            if (difference instanceof Key) {
                // 创建key
                Key key = (Key) difference;
                String deleteSql = key.getDeleteSql();
                sb.append(deleteSql);
                String createSql = key.getCreateSql();
                sb.append(createSql);
            } else if (difference instanceof Column) {
                // 更新字段
                Column column = (Column) difference;
                String updateSql = column.getUpdateSql();
                sb.append(updateSql).append("\n");
            }
        }
    }

    public String getSql() {
        if (ObjectUtils.isEmpty(this.delete) && ObjectUtils.isEmpty(this.create) && ObjectUtils.isEmpty(this.update)) {
            log.debug("数据库结构没有变化。");
            return null;
        }
        log.debug("开始生成sql...");
        StringBuilder sb = new StringBuilder();
        resolveDeleteSql(sb);
        resolveCreateSql(sb);
        resolveUpdateSql(sb);
        String sql = sb.toString();
        log.debug("sql生成结果:");
        log.debug(sql);
        return sql;
    }

    public void update() throws SQLException {
        if (ObjectUtils.isEmpty(this.create)
                && ObjectUtils.isEmpty(this.update)
                && ObjectUtils.isEmpty(this.delete)) {
            return;
        }
        String sql = getSql();
        String[] split = sql.split("\n");
        for (String s : split) {
            if (!ObjectUtils.isEmpty(s)) {
                DbUtil.execute(this.to.getConnection(), s);
            }
        }
    }

}
