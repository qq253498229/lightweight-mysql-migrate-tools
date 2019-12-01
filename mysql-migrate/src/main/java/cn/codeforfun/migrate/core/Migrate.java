package cn.codeforfun.migrate.core;

import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.Database;
import cn.codeforfun.migrate.core.entity.structure.DatabaseStructure;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.SQLException;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
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

    public Migrate from(Database sourceDatabase) {
        this.sourceDatabase = sourceDatabase;
        return this;
    }

    public Migrate to(Database targetDatabase) {
        this.targetDatabase = targetDatabase;
        return this;
    }

    public DiffResult diff() throws IOException, SQLException {
        if (sourceDatabase == null) {
            log.error("sourceDatabase 为空");
            throw new NullPointerException("sourceDatabase 不能为空");
        }

        if (targetDatabase == null) {
            log.error("targetDatabase 为空");
            throw new NullPointerException("targetDatabase 不能为空");
        }
        log.debug("开始对比数据库");
        DatabaseStructure source = resolveDatabase(this.sourceDatabase);
        DatabaseStructure target = resolveDatabase(this.targetDatabase);
        return new DiffResult().compare(source, target);
    }

    private static DatabaseStructure resolveDatabase(Database database) throws IOException, SQLException {
        DatabaseStructure databaseStructure = new DatabaseStructure();
        databaseStructure.configure(database);
        return databaseStructure;
    }
}
