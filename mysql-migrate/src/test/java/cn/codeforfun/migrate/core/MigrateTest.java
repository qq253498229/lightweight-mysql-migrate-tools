package cn.codeforfun.migrate.core;

import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

public class MigrateTest {

    private static final Integer FROM_PORT = 3306;
    private static final String FROM_HOST = "localhost";
    private static final String FROM_USERNAME = "root";
    private static final String FROM_PASSWORD = "root";
    private static final String FROM_TABLE = "test_db";

    private static final Integer TO_PORT = 3307;
    private static final String TO_HOST = "localhost";
    private static final String TO_USERNAME = "root";
    private static final String TO_PASSWORD = "root";
    private static final String TO_TABLE = "test_db";

    @Test
    public void diff() throws IOException, SQLException {
        DatabaseInfo from = new DatabaseInfo(FROM_HOST, FROM_PORT, FROM_TABLE, FROM_USERNAME, FROM_PASSWORD);
        DatabaseInfo to = new DatabaseInfo(TO_HOST, TO_PORT, TO_TABLE, TO_USERNAME, TO_PASSWORD);
        DiffResult diffResult = new Migrate().from(from).to(to).diff();
        String sql = diffResult.getSql();
        System.out.println(sql);
    }
}