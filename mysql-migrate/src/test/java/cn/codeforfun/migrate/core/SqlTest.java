package cn.codeforfun.migrate.core;

import cn.codeforfun.migrate.core.entity.Database;
import cn.codeforfun.migrate.core.utils.DbUtil;
import cn.codeforfun.migrate.core.utils.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class SqlTest {
    private static final Integer FROM_PORT = 3306;
    private static final String FROM_HOST = "localhost";
    private static final String FROM_USERNAME = "root";
    private static final String FROM_PASSWORD = "root";
    private static final String FROM_DATABASE = "test_db";

    @Test
    public void test() throws IOException, SQLException {
        String sql = FileUtil.getStringByClasspath("sql/database_structure.sql");
        Database database = new Database(FROM_HOST, FROM_PORT, FROM_DATABASE, FROM_USERNAME, FROM_PASSWORD);
        Connection connection = DbUtil.getConnection(database.getUrl(), database.getUsername(), database.getPassword());
        QueryRunner qr = new QueryRunner();
        List<Map<String, Object>> list = qr.query(connection, sql, new MapListHandler(), "test_db");
        for (Map<String, Object> param : list) {
            //todo
        }
        System.out.println(new ObjectMapper().writeValueAsString(list));
    }
}
