package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.Database;
import cn.codeforfun.migrate.core.entity.structure.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class DiffResult {

    private Database from;
    private Database to;

    private List<Difference> create = new ArrayList<>();
    private List<Difference> delete = new ArrayList<>();
    private List<Difference> update = new ArrayList<>();


    public String getSql() throws IOException {
        StringBuilder sb = new StringBuilder();
        resolveCreateSql(sb);
        return sb.toString();
    }

    private void resolveCreateSql(StringBuilder sb) throws IOException {
        for (Difference difference : this.create) {
            if (difference instanceof Table) {
                Table table = (Table) difference;
                String createSql = table.getCreateSql();
                sb.append(createSql).append("\n");
            }
        }
    }

    public DiffResult compare(Database from, Database to) {
        create.addAll(Difference.resolveCreate(from, to));
        delete.addAll(Difference.resolveDelete(from, to));
        update.addAll(Difference.resolveUpdate(from, to));
        return this;
    }


}
