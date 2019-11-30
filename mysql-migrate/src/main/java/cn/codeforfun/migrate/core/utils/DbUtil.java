package cn.codeforfun.migrate.core.utils;

import cn.codeforfun.migrate.core.exception.DatabaseConnectException;
import cn.codeforfun.migrate.core.exception.SqlExecuteException;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangbin
 */
@Slf4j
public class DbUtil {
    public static final String DATABASE_STRUCTURE_SQL_NAME = "sql/database_structure.sql";

    public static String getDatabaseStructureSql(Connection connection, String databaseName) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("show create database " + databaseName);
        String createDatabaseSql = null;
        if (rs.next()) {
            int columnCount = rs.getMetaData().getColumnCount();
            createDatabaseSql = rs.getString(columnCount);
        }
        return createDatabaseSql;
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
