package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 结构差异实体
 *
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class DiffResult {

    private Database from;
    private Database to;
    /**
     * 结构差异SQL
     */
    private String diffSql;
    private List<Difference> delete = new ArrayList<>();
    private List<Difference> create = new ArrayList<>();
    private List<Difference> update = new ArrayList<>();

    public DiffResult(Database from, Database to) {
        this.from = from;
        this.to = to;
    }

    /**
     * 获取差异SQL
     *
     * @return sql
     */
    public String getSql() {
        if (!ObjectUtils.isEmpty(this.diffSql)) {
            return this.diffSql;
        }
        if (ObjectUtils.isEmpty(this.delete)
                && ObjectUtils.isEmpty(this.create)
                && ObjectUtils.isEmpty(this.update)) {
            return null;
        }
        log.debug("开始生成sql...");
        StringBuilder sb = new StringBuilder();
        resolveDeleteSql(sb);
        resolveCreateSql(sb);
        resolveUpdateSql(sb);
        String sql = sb.toString();
        for (String s : sql.split("\n")) {
            log.debug("sql生成结果: {}", s);
        }
        this.diffSql = sql;
        return sql;
    }

    public void resolveDeleteSql(StringBuilder sb) {
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
                sb.append(deleteSql).append("\n");
            } else if (difference instanceof Column) {
                // 删除字段
                Column delete = (Column) difference;
                String deleteSql = delete.getDeleteSql();
                sb.append(deleteSql).append("\n");
            } else if (difference instanceof View) {
                // 删除view
                View delete = (View) difference;
                String deleteSql = delete.getDeleteSql();
                sb.append(deleteSql).append("\n");
            }
        }
    }

    public void resolveCreateSql(StringBuilder sb) {
        for (Difference difference : this.create) {
            if (difference instanceof Table) {
                // 创建表
                Table create = (Table) difference;
                String createSql = create.getCreateSql();
                sb.append(createSql).append("\n");
            } else if (difference instanceof Key) {
                // 创建key
                Key create = (Key) difference;
                String createSql = create.getCreateSql();
                sb.append(createSql).append("\n");
            } else if (difference instanceof Column) {
                // 创建字段
                Column create = (Column) difference;
                String createSql = create.getCreateSql();
                sb.append(createSql).append("\n");
            } else if (difference instanceof View) {
                // 创建view
                View create = (View) difference;
                String createSql = create.getCreateSql();
                sb.append(createSql).append("\n");
            }
        }
    }

    public void resolveUpdateSql(StringBuilder sb) {
        for (Difference difference : this.update) {
            if (difference instanceof Key) {
                // 创建key
                Key key = (Key) difference;
                String deleteSql = key.getDeleteSql();
                sb.append(deleteSql).append("\n");
                String createSql = key.getCreateSql();
                sb.append(createSql).append("\n");
            } else if (difference instanceof Column) {
                // 更新字段
                Column column = (Column) difference;
                String updateSql = column.getUpdateSql();
                sb.append(updateSql).append("\n");
            } else if (difference instanceof View) {
                View view = (View) difference;
                String updateSql = view.getUpdateSql();
                sb.append(updateSql).append("\n");
            }
        }
    }

}
