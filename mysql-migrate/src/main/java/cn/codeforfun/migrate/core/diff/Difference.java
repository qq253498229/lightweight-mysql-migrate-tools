package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.Column;
import cn.codeforfun.migrate.core.entity.structure.Database;
import cn.codeforfun.migrate.core.entity.structure.Table;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangbin
 */
public class Difference {
    public static Collection<? extends Difference> resolveCreate(Database from, Database to) {
        List<String> toTableNames = to.getTables().stream().map(Table::getName).collect(Collectors.toList());
        return from.getTables().stream().filter(s -> !toTableNames.contains(s.getName())).collect(Collectors.toList());
    }

    public static Collection<? extends Difference> resolveDelete(Database from, Database to) {
        List<String> fromTableNames = from.getTables().stream().map(Table::getName).collect(Collectors.toList());
        return to.getTables().stream().filter(s -> !fromTableNames.contains(s.getName())).collect(Collectors.toList());
    }

    public static Collection<? extends Difference> resolveUpdate(Database from, Database to) {
        List<Difference> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(from.getTables()) || CollectionUtils.isEmpty(to.getTables())) {
            return result;
        }
        for (Table fromTable : from.getTables()) {
            if (null == fromTable.getName()) {
                continue;
            }
            for (Table toTable : to.getTables()) {
                if (null == toTable.getName()) {
                    continue;
                }
                if (fromTable.getName().equals(toTable.getName())) {
                    result.addAll(resolveCreate(fromTable.getColumns(), toTable.getColumns()));
                    result.addAll(resolveDelete(fromTable.getColumns(), toTable.getColumns()));
                    result.addAll(resolveUpdate(fromTable.getColumns(), toTable.getColumns()));
                }
            }
        }
        return result;
    }

    public static Collection<? extends Difference> resolveCreate(List<Column> fromColumns, List<Column> toColumns) {
        //todo
        return new ArrayList<>();
    }

    public static Collection<? extends Difference> resolveDelete(List<Column> fromColumns, List<Column> toColumns) {
        //todo
        return new ArrayList<>();
    }

    public static Collection<? extends Difference> resolveUpdate(List<Column> fromColumns, List<Column> toColumns) {
        //todo
        return new ArrayList<>();
    }
}
