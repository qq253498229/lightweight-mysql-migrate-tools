package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.Database;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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


    public String getSql() {
        //todo
        return null;
    }

    public DiffResult compare(Database from, Database to) {
        create.addAll(Difference.resolveCreate(from, to));
        delete.addAll(Difference.resolveDelete(from, to));
        update.addAll(Difference.resolveUpdate(from, to));
        return this;
    }


}
