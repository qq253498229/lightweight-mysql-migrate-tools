package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.DatabaseStructure;
import cn.codeforfun.migrate.core.entity.structure.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
public class DiffResult {

    private DatabaseStructure from;
    private DatabaseStructure to;

    private List<Difference> createDifferenceList = new ArrayList<>();
    private List<Difference> deleteDifferenceList = new ArrayList<>();
    private List<Difference> updateDifferenceList = new ArrayList<>();


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
        List<Table> fromTables = from.getTables();
        List<Table> toTables = to.getTables();
        compareTable(fromTables, toTables);
    }

    private void compareTable(List<Table> fromTables, List<Table> toTables) {
        List<String> fromTableNameList = fromTables.stream().map(Table::getName).collect(Collectors.toList());
        List<String> toTableNameList = toTables.stream().map(Table::getName).collect(Collectors.toList());
        List<String> createTableList = toTableNameList.stream().filter(s -> !fromTableNameList.contains(s)).collect(Collectors.toList());
        log.info("createTableList:{}", createTableList);
    }
}
