package cn.codeforfun.test;

import cn.codeforfun.core.Migrate;
import cn.codeforfun.database.Database;
import cn.codeforfun.diff.DiffResult;
import org.junit.jupiter.api.Test;

public class DatabaseTest {

    @Test
    public void test() {
        Database from = new Database("localhost", 3306, "testdb", "root", "root");
        Database to = new Database("localhost", 3307, "testdb", "root", "root");
        Migrate migrate = new Migrate();
        DiffResult diffResult = migrate.diff(from, to);
    }
}
