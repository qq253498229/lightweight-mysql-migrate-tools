package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.Column;
import cn.codeforfun.migrate.core.entity.structure.Database;
import cn.codeforfun.migrate.core.entity.structure.Key;
import cn.codeforfun.migrate.core.entity.structure.Table;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangbin
 */
public interface Difference {
    static Collection<? extends Difference> resolveDelete(Database from, Database to) {
        //fixme
        List<String> fromTableNames = from.getTables().stream().map(Table::getName).collect(Collectors.toList());
        return to.getTables().stream().filter(s -> !fromTableNames.contains(s.getName())).collect(Collectors.toList());
    }

    static Collection<? extends Difference> resolveCreate(Database from, Database to) {
        //fixme
        List<String> toTableNames = to.getTables().stream().map(Table::getName).collect(Collectors.toList());
        return from.getTables().stream().filter(s -> !toTableNames.contains(s.getName())).collect(Collectors.toList());
    }

    static Collection<? extends Difference> resolveUpdate(Database from, Database to) {
        //fixme
        List<Difference> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(from.getTables()) || CollectionUtils.isEmpty(to.getTables())) {
            return result;
        }
        List<Key> fromKeyList = new ArrayList<>();
        from.getTables().forEach(s -> fromKeyList.addAll(s.getKeys()));
        List<Key> toKeyList = new ArrayList<>();
        to.getTables().forEach(s -> toKeyList.addAll(s.getKeys()));

        result.addAll(resolveDeleteKey(fromKeyList, toKeyList));
        result.addAll(resolveCreateKey(fromKeyList, toKeyList));

        List<Column> fromColumnList = new ArrayList<>();
        from.getTables().forEach(s -> fromColumnList.addAll(s.getColumns()));
        List<Column> toColumnList = new ArrayList<>();
        to.getTables().forEach(s -> toColumnList.addAll(s.getColumns()));

        result.addAll(resolveDelete(fromColumnList, toColumnList));
        result.addAll(resolveCreate(fromColumnList, toColumnList));
        result.addAll(resolveUpdate(fromColumnList, toColumnList));

        return result;
    }

    static Collection<? extends Difference> resolveDeleteKey(List<Key> fromKeyList, List<Key> toKeyList) {
        List<String> fromKeyNameList = fromKeyList.stream().map(Key::getName).collect(Collectors.toList());
        return toKeyList.stream().filter(s -> !fromKeyNameList.contains(s.getName())).collect(Collectors.toList());
    }

    static Collection<? extends Difference> resolveCreateKey(List<Key> fromKeyList, List<Key> toKeyList) {
        List<String> toKeyNameList = toKeyList.stream().map(Key::getName).collect(Collectors.toList());
        return fromKeyList.stream().filter(s -> !toKeyNameList.contains(s.getName())).collect(Collectors.toList());
    }

    static Collection<? extends Difference> resolveDelete(List<Column> fromColumns, List<Column> toColumns) {
        //todo
        return new ArrayList<>();
    }

    static Collection<? extends Difference> resolveCreate(List<Column> fromColumns, List<Column> toColumns) {
        //todo
        return new ArrayList<>();
    }

    static Collection<? extends Difference> resolveUpdate(List<Column> fromColumns, List<Column> toColumns) {
        //todo
        return new ArrayList<>();
    }
}
