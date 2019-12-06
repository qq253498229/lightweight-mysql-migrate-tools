package cn.codeforfun.migrate.core.diff;

/**
 * 差异接口
 *
 * @author wangbin
 */
public interface Difference {
    /**
     * 获取差异创建SQL
     *
     * @return sql
     */
    String getCreateSql();

    /**
     * 获取差异更新SQL
     *
     * @return sql
     */
    String getUpdateSql();

    /**
     * 获取差异删除sql
     *
     * @return sql
     */
    String getDeleteSql();
}
