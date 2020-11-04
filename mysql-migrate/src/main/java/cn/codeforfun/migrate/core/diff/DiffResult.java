package cn.codeforfun.migrate.core.diff;

import cn.codeforfun.migrate.core.entity.structure.*;
import cn.codeforfun.migrate.core.utils.ObjectUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 结构差异实体
 *
 * @author wangbin
 */
@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class DiffResult {

    private Database from;
    private Database to;
    /**
     * 结构差异SQL
     */
    private List<String> sqlList = new ArrayList<>();
    private List<Difference> delete = new ArrayList<>();
    private List<Difference> create = new ArrayList<>();
    private List<Difference> update = new ArrayList<>();

    public DiffResult(Database from, Database to) {
        this.from = from;
        this.to = to;
    }

    /**
     * 获取差异SQL
     *
     * @return sql
     */
    @JsonIgnore
    public List<String> getSqlList() {
        if (!ObjectUtils.isEmpty(this.sqlList)) {
            return this.sqlList;
        }
        if (ObjectUtils.isEmpty(this.delete)
                && ObjectUtils.isEmpty(this.create)
                && ObjectUtils.isEmpty(this.update)) {
            return new ArrayList<>();
        }
        log.debug("开始生成sql...");
        resolveDeleteSql();
        resolveCreateSql();
        resolveUpdateSql();
        log.debug("生成sql完成");
        log.trace("sql生成结果: {}", sqlList);
        return sqlList;
    }

    public void resolveDeleteSql() {
        for (Difference difference : this.delete) {
            if (difference instanceof Table) {
                Table delete = (Table) difference;
                if (delete.hasForeignKey()) {
                    // 先判断有没有外键，如果有就先删除外键
                    String deleteForeignKeySql = delete.getDeleteForeignKeySql();
                    this.sqlList.add(deleteForeignKeySql);
                }
                // 再删除表
                String deleteSql = delete.getDeleteSql();
                this.sqlList.add(deleteSql);
            } else if (difference instanceof Key) {
                // 删除key
                Key delete = (Key) difference;
                String deleteSql = delete.getDeleteSql();
                this.sqlList.add(deleteSql);
            } else if (difference instanceof Column) {
                // 删除字段
                Column delete = (Column) difference;
                String deleteSql = delete.getDeleteSql();
                this.sqlList.add(deleteSql);
            } else if (difference instanceof View) {
                // 删除view
                View delete = (View) difference;
                String deleteSql = delete.getDeleteSql();
                this.sqlList.add(deleteSql);
            } else if (difference instanceof Function) {
                // 删除function
                Function delete = (Function) difference;
                String deleteSql = delete.getDeleteSql();
                this.sqlList.add(deleteSql);
            } else if (difference instanceof Procedure) {
                // 删除Procedure
                Procedure delete = (Procedure) difference;
                String deleteSql = delete.getDeleteSql();
                this.sqlList.add(deleteSql);
            } else if (difference instanceof Trigger) {
                // 删除trigger
                Trigger delete = (Trigger) difference;
                String deleteSql = delete.getDeleteSql();
                this.sqlList.add(deleteSql);
            }
        }
    }

    public void resolveCreateSql() {
        for (Difference difference : this.create) {
            if (difference instanceof Table) {
                // 创建表
                Table create = (Table) difference;
                String createSql = create.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Key) {
                // 创建key
                Key create = (Key) difference;
                String createSql = create.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Column) {
                // 创建字段
                Column create = (Column) difference;
                String createSql = create.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof View) {
                // 创建view
                View create = (View) difference;
                String createSql = create.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Function) {
                // 创建Function
                Function create = (Function) difference;
                String createSql = create.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Procedure) {
                // 创建Procedure
                Procedure create = (Procedure) difference;
                String createSql = create.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Trigger) {
                // 创建Trigger
                Trigger create = (Trigger) difference;
                String createSql = create.getCreateSql();
                this.sqlList.add(createSql);
            }
        }
    }

    public void resolveUpdateSql() {
        for (Difference difference : this.update) {
            if (difference instanceof Key) {
                // 创建key
                Key update = (Key) difference;
                String deleteSql = update.getDeleteSql();
                this.sqlList.add(deleteSql);
                String createSql = update.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Column) {
                // 更新字段
                Column update = (Column) difference;
                String updateSql = update.getUpdateSql();
                this.sqlList.add(updateSql);
            } else if (difference instanceof View) {
                // 更新view
                View update = (View) difference;
                String updateSql = update.getUpdateSql();
                this.sqlList.add(updateSql);
            } else if (difference instanceof Function) {
                // 更新function
                Function update = (Function) difference;
                String deleteSql = update.getDeleteSql();
                this.sqlList.add(deleteSql);
                String createSql = update.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Procedure) {
                // 更新Procedure
                Procedure update = (Procedure) difference;
                String deleteSql = update.getDeleteSql();
                this.sqlList.add(deleteSql);
                String createSql = update.getCreateSql();
                this.sqlList.add(createSql);
            } else if (difference instanceof Trigger) {
                // 更新Trigger
                Trigger update = (Trigger) difference;
                String deleteSql = update.getDeleteSql();
                this.sqlList.add(deleteSql);
                String createSql = update.getCreateSql();
                this.sqlList.add(createSql);
            }
        }
    }

}
