package cn.codeforfun.migrate.core.json;

import cn.codeforfun.migrate.core.Migrate;
import cn.codeforfun.migrate.core.diff.DiffResult;
import cn.codeforfun.migrate.core.entity.DatabaseInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.Test;

import java.sql.SQLException;

public class ConvertJsonTest {
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
    @Ignore
    public void test() throws SQLException, JsonProcessingException {
        DatabaseInfo from = new DatabaseInfo(FROM_HOST, FROM_PORT, FROM_TABLE, FROM_USERNAME, FROM_PASSWORD);
        DatabaseInfo to = new DatabaseInfo(TO_HOST, TO_PORT, TO_TABLE, TO_USERNAME, TO_PASSWORD);
        Migrate migrate = new Migrate().from(from).to(to);
        DiffResult diffResult = migrate.diff();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(diffResult);
        System.out.println(json);
    }
}
