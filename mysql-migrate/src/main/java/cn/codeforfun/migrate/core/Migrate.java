package cn.codeforfun.migrate.core;

import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.codeforfun.migrate.core.entity.structure.Database;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class Migrate {
    private DatabaseInfo sourceInfo;
    private DatabaseInfo targetInfo;

    public Migrate(DatabaseInfo targetInfo) {
        this.targetInfo = targetInfo;
    }

    public Migrate(DatabaseInfo sourceInfo, DatabaseInfo targetInfo) {
        this.sourceInfo = sourceInfo;
        this.targetInfo = targetInfo;
    }

    public Migrate from(DatabaseInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
        return this;
    }

    public Migrate to(DatabaseInfo targetInfo) {
        this.targetInfo = targetInfo;
        return this;
    }

    public DiffResult diff() throws SQLException {
        if (sourceInfo == null) {
            log.error("sourceDatabase 为空");
            throw new NullPointerException("sourceDatabase 不能为空");
        }
        if (targetInfo == null) {
            log.error("targetDatabase 为空");
            throw new NullPointerException("targetDatabase 不能为空");
        }
        log.debug("开始对比数据库");
        Database source = new Database().init(this.sourceInfo);
        Database target = new Database().init(this.targetInfo);
        return new DiffResult(source, target).compare();
    }

}
