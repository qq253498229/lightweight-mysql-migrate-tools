package cn.codeforfun.core;

import cn.codeforfun.core.entity.Database;
import cn.codeforfun.core.diff.DiffResult;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author wangbin
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
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
        if (sourceDatabase == null) {
            log.error("sourceDatabase 为空");
            throw new NullPointerException("sourceDatabase 不能为空");
        }

        if (targetDatabase == null) {
            log.error("targetDatabase 为空");
            throw new NullPointerException("targetDatabase 不能为空");
        }
        log.debug("开始对比数据库");


        log.debug("对比完成");
        return new DiffResult();
    }
}
