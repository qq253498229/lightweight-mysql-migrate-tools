package cn.codeforfun.migrate.core;

import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

@Slf4j
public class MigrateTest {

    private static final Integer FROM_PORT = 3306;
    private static final String FROM_HOST = "localhost";
    private static final String FROM_USERNAME = "root";
    private static final String FROM_PASSWORD = "root";
    private static final String FROM_DB = "test";

    private static final Integer TO_PORT = 3306;
    private static final String TO_HOST = "localhost";
    private static final String TO_USERNAME = "root";
    private static final String TO_PASSWORD = "root";
    private static final String TO_DB = "test1";

    /**
     * 显示数据库建表语句
     */
    @Test
    @Ignore
    public void showSql() throws SQLException {
        DatabaseInfo info = new DatabaseInfo(FROM_HOST, FROM_PORT, FROM_USERNAME, FROM_PASSWORD, FROM_DB);
        Migrate migrate = new Migrate().ignoreCharacterCompare();
        List<String> sql = migrate.showSql(info);
        for (String s : sql) {
            System.out.println(s);
        }
    }

    /**
     * 对比数据库
     */
    @Test
    @Ignore
    public void diff() throws SQLException {
        DatabaseInfo from = new DatabaseInfo(FROM_HOST, FROM_PORT, FROM_USERNAME, FROM_PASSWORD, FROM_DB);
        DatabaseInfo to = new DatabaseInfo(TO_HOST, TO_PORT, TO_USERNAME, TO_PASSWORD, TO_DB);
        Migrate migrate = new Migrate().from(from).to(to).ignoreCharacterCompare();
        DiffResult diffResult = migrate.diff();
        List<String> sqlList = diffResult.getSqlList();
        for (String sql : sqlList) {
            System.out.println(sql);
        }
    }

    /**
     * 更新数据库
     */
    @Test
    @Ignore
    public void update() throws SQLException {
        DatabaseInfo from = new DatabaseInfo(FROM_HOST, FROM_PORT, FROM_USERNAME, FROM_PASSWORD, FROM_DB);
        DatabaseInfo to = new DatabaseInfo(TO_HOST, TO_PORT, TO_USERNAME, TO_PASSWORD, TO_DB);
        Migrate migrate = new Migrate().from(from).to(to).ignoreCharacterCompare();
        migrate.update();
    }

}