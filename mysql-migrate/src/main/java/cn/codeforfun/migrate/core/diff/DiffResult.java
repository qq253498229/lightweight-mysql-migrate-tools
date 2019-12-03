package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.Column;
import cn.codeforfun.migrate.core.entity.structure.Database;
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

    private void resolveCreateSql(StringBuilder sb) {
        for (Difference difference : this.create) {
            if (difference instanceof Table) {
                Table table = (Table) difference;
                String createSql = table.getCreateSql();
                sb.append(createSql).append("\n");
            }
        }
    }

    private void resolveDeleteSql(StringBuilder sb) {
        // 先删除外键
        for (Difference difference : this.delete) {
            if (difference instanceof Table) {
                Table table = (Table) difference;
                String deleteSql = table.getDeleteForeignKeySql();
                sb.append(deleteSql);
            }
        }
        // 再删除表
        for (Difference difference : this.delete) {
            if (difference instanceof Table) {
                Table table = (Table) difference;
                String deleteSql = table.getDeleteSql();
                sb.append(deleteSql).append("\n");
            }
        }
    }

    private void resolveUpdateSql(StringBuilder sb) {
        for (Difference difference : this.update) {
            if (difference instanceof Column) {
                Column column = (Column) difference;
                String updateSql = column.getUpdateSql();
                sb.append(updateSql).append("\n");
            }
        }
    }

    public DiffResult compare() {
        delete.addAll(Difference.resolveDelete(this.from, this.to));
        create.addAll(Difference.resolveCreate(this.from, this.to));
        update.addAll(Difference.resolveUpdate(this.from, this.to));
        return this;
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
