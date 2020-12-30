package cn.codeforfun.core;

import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import lombok.Data;

/**
 * @author wangbin
 */
@Data
public class Diff {
    private DatabaseInfo source;
    private DatabaseInfo target;
}
