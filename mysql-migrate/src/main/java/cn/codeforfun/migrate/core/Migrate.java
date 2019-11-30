package cn.codeforfun.migrate.core;

import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.Database;
import cn.codeforfun.migrate.core.entity.structure.DatabaseStructure;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class Migrate {
    private Database sourceDatabase;
    private Database targetDatabase;

    private Migrate() {
    }

    public Migrate(Database targetDatabase) {
        this.targetDatabase = targetDatabase;
    }

    public Migrate(Database sourceDatabase, Database targetDatabase) {
        this.sourceDatabase = sourceDatabase;
        this.targetDatabase = targetDatabase;
    }

    public static DiffResult diff(Database sourceDatabase, Database targetDatabase) {
        if (sourceDatabase == null) {
            log.error("sourceDatabase 为空");
            throw new NullPointerException("sourceDatabase 不能为空");
        }

        if (targetDatabase == null) {
            log.error("targetDatabase 为空");
            throw new NullPointerException("targetDatabase 不能为空");
        }
        log.debug("开始对比数据库");
        return compare(sourceDatabase, targetDatabase);
    }

    private static DiffResult compare(Database sourceDatabase, Database targetDatabase) {
        DatabaseStructure source = resolveDatabase(sourceDatabase);
        DatabaseStructure target = resolveDatabase(targetDatabase);
        return null;
    }

    private static DatabaseStructure resolveDatabase(Database database) {
        DatabaseStructure databaseStructure = new DatabaseStructure();
        databaseStructure.config(database);
        return databaseStructure;
    }
}
