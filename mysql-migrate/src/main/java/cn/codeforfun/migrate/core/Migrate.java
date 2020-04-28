package cn.codeforfun.migrate.core;

import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import cn.codeforfun.migrate.core.entity.structure.*;
import cn.codeforfun.migrate.core.utils.DbUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 迁移核心类
 *
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class Migrate {
    /**
     * 源数据库信息
     */
    private DatabaseInfo sourceInfo;
    /**
     * 目标数据库信息
     */
    private DatabaseInfo targetInfo;
    /**
     * 结构差异实体
     */
    private DiffResult diff;

    public Migrate(DatabaseInfo targetInfo) {
        this.targetInfo = targetInfo;
    }

    public Migrate(DatabaseInfo sourceInfo, DatabaseInfo targetInfo) {
        this.sourceInfo = sourceInfo;
        this.targetInfo = targetInfo;
    }

    /**
     * 设置源数据库信息
     *
     * @param sourceInfo 源数据库信息
     * @return 核心类
     */
    public Migrate from(DatabaseInfo sourceInfo) {
        this.sourceInfo = sourceInfo;
        return this;
    }

    /**
     * 设置目标数据库信息
     *
     * @param targetInfo 目标数据库信息
     * @return 核心类
     */
    public Migrate to(DatabaseInfo targetInfo) {
        this.targetInfo = targetInfo;
        return this;
    }

    /**
     * 对比数据库结构
     */
    private void compare() {
        List<Table> fromTableList = this.diff.getFrom().getTables();
        List<Table> toTableList = this.diff.getTo().getTables();
        List<String> fromTableNameList = fromTableList.stream().map(Table::getName).collect(Collectors.toList());
        List<String> toTableNameList = toTableList.stream().map(Table::getName).collect(Collectors.toList());
        // 需要更新的表
        List<Table> fromUpdateTableList = fromTableList.stream().filter(s -> toTableNameList.contains(s.getName())).collect(Collectors.toList());
        List<Table> toUpdateTableList = toTableList.stream().filter(s -> fromTableNameList.contains(s.getName())).collect(Collectors.toList());

        compareKey(fromUpdateTableList, toUpdateTableList);
        compareColumn(fromUpdateTableList, toUpdateTableList);
        compareTable(fromTableList, toTableList, fromTableNameList, toTableNameList);
        compareView();
        compareFunction();
        compareProcedure();
        compareTrigger();
    }

    /**
     * 对比trigger
     */
    private void compareTrigger() {
        // trigger
        List<Trigger> fromTriggerList = this.diff.getFrom().getTriggers();
        List<Trigger> toTriggerList = this.diff.getTo().getTriggers();
        List<String> fromTriggerNameList = fromTriggerList.stream().map(Trigger::getName).collect(Collectors.toList());
        List<String> toTriggerNameList = toTriggerList.stream().map(Trigger::getName).collect(Collectors.toList());
        // 删除trigger
        List<Trigger> deleteTriggerList = toTriggerList.stream().filter(s -> !fromTriggerNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getDelete().addAll(deleteTriggerList);
        // 新建trigger
        List<Trigger> createTriggerList = fromTriggerList.stream().filter(s -> !toTriggerNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getCreate().addAll(createTriggerList);
        // 更新trigger
        List<Trigger> updateTriggerList = toTriggerList.stream().map(s -> fromTriggerList.stream().filter(j ->
                s.getName().equals(j.getName())
                        && !s.equals(j)).collect(Collectors.toList())
        ).flatMap(List::stream).collect(Collectors.toList());
        this.diff.getUpdate().addAll(updateTriggerList);
    }

    /**
     * 对比procedure
     */
    private void compareProcedure() {
        // Procedure
        List<Procedure> fromProcedureList = this.diff.getFrom().getProcedures();
        List<Procedure> toProcedureList = this.diff.getTo().getProcedures();
        List<String> fromProcedureNameList = fromProcedureList.stream().map(Procedure::getName).collect(Collectors.toList());
        List<String> toProcedureNameList = toProcedureList.stream().map(Procedure::getName).collect(Collectors.toList());
        // 删除Procedure
        List<Procedure> deleteProcedureList = toProcedureList.stream().filter(s -> !fromProcedureNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getDelete().addAll(deleteProcedureList);
        // 新建Procedure
        List<Procedure> createProcedureList = fromProcedureList.stream().filter(s -> !toProcedureNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getCreate().addAll(createProcedureList);
        // 更新Procedure
        List<Procedure> updateProcedureList = toProcedureList.stream().map(s -> fromProcedureList.stream().filter(j ->
                s.getName().equals(j.getName())
                        && !s.equals(j)).collect(Collectors.toList())
        ).flatMap(List::stream).collect(Collectors.toList());
        this.diff.getUpdate().addAll(updateProcedureList);
    }

    /**
     * 对比function
     */
    private void compareFunction() {
        // function
        List<Function> fromFunctionList = this.diff.getFrom().getFunctions();
        List<Function> toFunctionList = this.diff.getTo().getFunctions();
        List<String> fromFunctionNameList = fromFunctionList.stream().map(Function::getName).collect(Collectors.toList());
        List<String> toFunctionNameList = toFunctionList.stream().map(Function::getName).collect(Collectors.toList());
        // 删除Function
        List<Function> deleteFunctionList = toFunctionList.stream().filter(s -> !fromFunctionNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getDelete().addAll(deleteFunctionList);
        // 新建Function
        List<Function> createFunctionList = fromFunctionList.stream().filter(s -> !toFunctionNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getCreate().addAll(createFunctionList);
        // 更新function
        List<Function> updateFunctionList = toFunctionList.stream().map(s -> fromFunctionList.stream().filter(j ->
                s.getName().equals(j.getName())
                        && !s.equals(j)).collect(Collectors.toList())
        ).flatMap(List::stream).collect(Collectors.toList());
        this.diff.getUpdate().addAll(updateFunctionList);
    }

    /**
     * 对比view
     */
    private void compareView() {
        // view
        List<View> fromViewList = this.diff.getFrom().getViews();
        List<View> toViewList = this.diff.getTo().getViews();
        List<String> fromViewNameList = fromViewList.stream().map(View::getName).collect(Collectors.toList());
        List<String> toViewNameList = toViewList.stream().map(View::getName).collect(Collectors.toList());
        // 删除view
        List<View> deleteViewList = toViewList.stream().filter(s -> !fromViewNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getDelete().addAll(deleteViewList);
        // 新建view
        List<View> createViewList = fromViewList.stream().filter(s -> !toViewNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getCreate().addAll(createViewList);
        // 更新view
        List<View> updateViewList = toViewList.stream().map(s -> fromViewList.stream().filter(j ->
                s.getName().equals(j.getName())
                        && !s.equals(j)).collect(Collectors.toList())
        ).flatMap(List::stream).collect(Collectors.toList());
        this.diff.getUpdate().addAll(updateViewList);
    }

    /**
     * 对比column
     *
     * @param fromUpdateTableList 需要更新的源表
     * @param toUpdateTableList   需要更新的目标表
     */
    private void compareColumn(List<Table> fromUpdateTableList, List<Table> toUpdateTableList) {
        // 字段
        List<Column> fromColumnList = fromUpdateTableList.stream().map(Table::getColumns).flatMap(List::stream).collect(Collectors.toList());
        List<Column> toColumnList = toUpdateTableList.stream().map(Table::getColumns).flatMap(List::stream).collect(Collectors.toList());
        List<String> fromColumnNameList = fromColumnList.stream().map(Column::getName).collect(Collectors.toList());
        List<String> toColumnNameList = toColumnList.stream().map(Column::getName).collect(Collectors.toList());
        // 删除字段
        List<Column> deleteColumnList = toColumnList.stream().filter(s -> !fromColumnNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getDelete().addAll(deleteColumnList);
        // 新建字段
        List<Column> createColumnList = fromColumnList.stream().filter(s -> !toColumnNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getCreate().addAll(createColumnList);
        // 更新字段
        List<Column> updateColumnList = toColumnList.stream().map(s -> fromColumnList.stream().filter(j ->
                s.getName().equals(j.getName())
                        && s.getTable().equals(j.getTable())
                        && !s.equals(j)
        ).collect(Collectors.toList())).flatMap(List::stream).collect(Collectors.toList());
        this.diff.getUpdate().addAll(updateColumnList);
    }

    /**
     * 对比key
     *
     * @param fromUpdateTableList 需要更新的源表
     * @param toUpdateTableList   需要更新的目标表
     */
    private void compareKey(List<Table> fromUpdateTableList, List<Table> toUpdateTableList) {
        // key
        List<Key> fromKeyList = fromUpdateTableList.stream().map(Table::getKeys).flatMap(List::stream).collect(Collectors.toList());
        List<Key> toKeyList = toUpdateTableList.stream().map(Table::getKeys).flatMap(List::stream).collect(Collectors.toList());
        List<String> fromKeyNameList = fromKeyList.stream().map(Key::getName).collect(Collectors.toList());
        List<String> toKeyNameList = toKeyList.stream().map(Key::getName).collect(Collectors.toList());
        // 删除key
        List<Key> deleteKeyList = toKeyList.stream().filter(s -> !fromKeyNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getDelete().addAll(deleteKeyList);
        // 新建key
        List<Key> createKeyList = fromKeyList.stream().filter(s -> !toKeyNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getCreate().addAll(createKeyList);
        // 更新key
        List<Key> updateKeyList = new ArrayList<>();
        for (Key fromKey : fromKeyList) {
            for (Key toKey : toKeyList) {
                if (fromKey.getName().equals(toKey.getName())
                        && fromKey.getTableName().equals(toKey.getTableName())
                        && !fromKey.equals(toKey)) {
                    updateKeyList.add(fromKey);
                }
            }
        }
        this.diff.getUpdate().addAll(updateKeyList);
    }

    /**
     * 对比table
     *
     * @param fromTableList     源表
     * @param toTableList       目标表
     * @param fromTableNameList 源表名数组
     * @param toTableNameList   目标表名数组
     */
    private void compareTable(List<Table> fromTableList, List<Table> toTableList, List<String> fromTableNameList, List<String> toTableNameList) {
        // 删除表
        List<Table> deleteTableList = toTableList.stream().filter(s -> !fromTableNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getDelete().addAll(deleteTableList);
        // 新建表
        List<Table> createTableList = fromTableList.stream().filter(s -> !toTableNameList.contains(s.getName())).collect(Collectors.toList());
        this.diff.getCreate().addAll(createTableList);
    }

    /**
     * 对比源数据库和目标数据库
     *
     * @return 结构差异
     * @throws SQLException SQL异常
     */
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
        this.diff = new DiffResult(source, target);
        compare();
        log.debug("对比数据库完成");
        if (ObjectUtils.isEmpty(this.diff.getDelete())
                && ObjectUtils.isEmpty(this.diff.getCreate())
                && ObjectUtils.isEmpty(this.diff.getUpdate())) {
            log.debug("数据库结构没有变化。");
        }
        return this.diff;
    }

    /**
     * 同步结构
     *
     * @throws SQLException SQL异常
     */
    public void update() throws SQLException {
        if (this.diff == null) {
            diff();
        }
        if (ObjectUtils.isEmpty(this.diff.getDelete())
                && ObjectUtils.isEmpty(this.diff.getCreate())
                && ObjectUtils.isEmpty(this.diff.getUpdate())) {
            return;
        }
        log.debug("开始同步数据库");
        List<String> sqlList = this.diff.getSqlList();
        for (String sql : sqlList) {
            DbUtil.execute(this.diff.getTo().getConnection(), sql);
        }
        log.debug("数据库同步完成");
    }
}
