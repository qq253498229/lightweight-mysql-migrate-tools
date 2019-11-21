package cn.codeforfun.utils;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangbin
 */
public class DbUtil {

    public static DataSource getDataSource(String url, String username, String password) {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxWait(5000);
        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("数据库连接失败");
        }
        return dataSource;
    }

    public static Object query(DataSource dataSource, String sql) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        ResultSetHandler<Object> resultList = rs -> {
            if (!rs.next()) {
                return null;
            }

            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            Object[] result = new Object[cols];

            for (int i = 0; i < cols; i++) {
                result[i] = rs.getObject(i + 1);
            }

            return result;
        };
        return runner.query(sql, resultList);
    }

    public static <T> List<T> queryList(DataSource dataSource, String sql, Class<T> clazz) throws SQLException {
        QueryRunner runner = new QueryRunner(dataSource);
        ResultSetHandler<List<T>> resultList = rs -> {
            if (!rs.next()) {
                return null;
            }
            ResultSetMetaData meta = rs.getMetaData();
            int cols = meta.getColumnCount();
            List<T> result = new ArrayList<>();
            for (int i = 0; i < cols; i++) {
                result.add(rs.getObject(i + 1, clazz));
            }
            return result;
        };
        return runner.query(sql, resultList);
    }
}
