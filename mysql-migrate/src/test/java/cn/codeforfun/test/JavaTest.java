package cn.codeforfun.test;

import cn.codeforfun.migrate.core.utils.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static cn.codeforfun.migrate.core.utils.DbUtil.DATABASE_STRUCTURE_SQL_NAME;

public class JavaTest {
    @Test
    public void test() throws IOException {
        String sql = FileUtil.getStringByClasspath(DATABASE_STRUCTURE_SQL_NAME);
        System.out.println(sql);
    }
}
