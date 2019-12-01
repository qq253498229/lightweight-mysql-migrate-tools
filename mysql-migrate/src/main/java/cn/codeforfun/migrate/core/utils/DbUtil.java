package cn.codeforfun.migrate.core.utils;

import cn.codeforfun.migrate.core.exception.DatabaseConnectException;
import cn.codeforfun.migrate.core.exception.SqlExecuteException;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangbin
 */
@Slf4j
public class DbUtil {
    public static final String DATABASE_STRUCTURE_SQL_NAME = "sql/database.sql";

    public static <T> List<T> getBeanList(Connection connection, String sql, Class<T> clazz) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanListHandler<T>(clazz));
    }

    public static <T> List<T> getBeanList(Connection connection, String sql, Class<T> clazz, Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanListHandler<T>(clazz), params);
    }

    public static <T> T getBean(Connection connection, String sql, Class<T> clazz) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanHandler<>(clazz));
    }

    public static <T> T getBean(Connection connection, String sql, Class<T> clazz, Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanHandler<>(clazz, new BasicRowProcessor(new BeanProcessor(MigrateBeanTools.customColumn(clazz)))), params);
    }

    public static Map<String, Object> getDatabaseStructure(Connection connection, String databaseName) throws SQLException, IOException {
        String sql = FileUtil.getStringByClasspath("sql/database.sql");
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new MapHandler(), databaseName);
    }

    public static List<Map<String, Object>> getTableStructure(Connection connection, String databaseName) throws SQLException, IOException {
        String sql = FileUtil.getStringByClasspath("sql/table.sql");
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new MapListHandler(), databaseName);
    }

    public static Connection getConnection(String url, String username, String password) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            log.error("数据库连接失败,url:{},user:{},password:{}", url, username, password);
            throw new DatabaseConnectException("数据库连接失败");
        }
        return connection;
    }

    public static List<String> executeSql(Connection connection, String sql) {
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            List<String> tableNameList = new ArrayList<>();
            while (rs.next()) {
                int columnCount = rs.getMetaData().getColumnCount();
                String tableName = rs.getString(columnCount);
                tableNameList.add(tableName);
            }
            return tableNameList;
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new SqlExecuteException(e.getMessage());
        }
    }

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
