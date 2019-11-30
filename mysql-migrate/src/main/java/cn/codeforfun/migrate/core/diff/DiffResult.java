package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.DatabaseStructure;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangbin
 */
@Getter
@Setter
public class DiffResult {

    private DatabaseStructure from;
    private DatabaseStructure to;



    public String getSQL() {
        return null;
    }

    public DiffResult compare(DatabaseStructure from, DatabaseStructure to) {
        this.from = from;
        this.to = to;
        compare();
        return this;
    }

    private void compare() {

    }
}
