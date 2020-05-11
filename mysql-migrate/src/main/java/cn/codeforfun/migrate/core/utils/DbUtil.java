package cn.codeforfun.migrate.core.utils;

import cn.codeforfun.migrate.core.exception.DatabaseConnectException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.BasicRowProcessor;
import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * DbUtil工具类
 *
 * @author wangbin
 */
@Slf4j
public class DbUtil {
    public static <T> List<T> getBeanList(Connection connection, String sql, Class<T> clazz) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanListHandler<>(clazz, new BasicRowProcessor(new BeanProcessor(MigrateBeanTools.customColumn(clazz)))));
    }

    public static <T> List<T> getBeanList(Connection connection, String sql, Class<T> clazz, Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanListHandler<>(clazz, new BasicRowProcessor(new BeanProcessor(MigrateBeanTools.customColumn(clazz)))), params);
    }

    public static <T> T getBean(Connection connection, String sql, Class<T> clazz) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanHandler<>(clazz, new BasicRowProcessor(new BeanProcessor(MigrateBeanTools.customColumn(clazz)))));
    }

    public static <T> T getBean(Connection connection, String sql, Class<T> clazz, Object... params) throws SQLException {
        QueryRunner runner = new QueryRunner();
        return runner.query(connection, sql, new BeanHandler<>(clazz, new BasicRowProcessor(new BeanProcessor(MigrateBeanTools.customColumn(clazz)))), params);
    }

    public static Connection getConnection(String url, String username, String password) {
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            log.error("数据库连接失败,url:{},user:{}", url, username);
            throw new DatabaseConnectException("数据库连接失败,url:" + url + ",user:" + username);
        }
        return connection;
    }

    public static void execute(Connection connection, String sql) throws SQLException {
        QueryRunner runner = new QueryRunner();
        runner.update(connection, sql);
    }
}
