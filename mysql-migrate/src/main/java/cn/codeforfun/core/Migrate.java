package cn.codeforfun.core;

import cn.codeforfun.database.Database;
import cn.codeforfun.diff.DiffResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangbin
 */
@Getter
@Setter
@NoArgsConstructor
public class Migrate {
    private Database sourceDatabase;
    private Database targetDatabase;

    public Migrate(Database targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    public Migrate(Database sourceDatabase, Database targetDatabase) {
        this.sourceDatabase = sourceDatabase;
        this.targetDatabase = targetDatabase;
    }

    public DiffResult diff(Database sourceDatabase, Database targetDatabase) {
        return null;
    }
}
