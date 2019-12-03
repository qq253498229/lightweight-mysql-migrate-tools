package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.Database;
import cn.codeforfun.migrate.core.entity.structure.Table;
import cn.codeforfun.migrate.core.utils.DbUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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

    private List<Difference> create = new ArrayList<>();
    private List<Difference> delete = new ArrayList<>();
    private List<Difference> update = new ArrayList<>();

    public DiffResult(Database from, Database to) {
        this.from = from;
        this.to = to;
    }

    public String getSql() {
        StringBuilder sb = new StringBuilder();
        resolveCreateSql(sb);
        String sql = sb.toString();
        log.debug("生成sql:{}", sql);
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

    public DiffResult compare() {
        create.addAll(Difference.resolveCreate(this.from, this.to));
        delete.addAll(Difference.resolveDelete(this.from, this.to));
        update.addAll(Difference.resolveUpdate(this.from, this.to));
        return this;
    }


    public void update() throws SQLException {
        String sql = getSql();
        DbUtil.execute(this.to.getConnection(), sql);
    }
}
